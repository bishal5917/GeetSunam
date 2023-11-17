package com.example.geetsunam.features.presentation.login

import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.geetsunam.MainActivity
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivityLoginBinding
import com.example.geetsunam.features.presentation.login.forgot_password.ForgotPasswordActivity
import com.example.geetsunam.features.presentation.login.google_login_viewmodel.GoogleLoginEvent
import com.example.geetsunam.features.presentation.login.google_login_viewmodel.GoogleLoginState
import com.example.geetsunam.features.presentation.login.google_login_viewmodel.GoogleLoginViewModel
import com.example.geetsunam.features.presentation.login.viewmodel.LoginEvent
import com.example.geetsunam.features.presentation.login.viewmodel.LoginState
import com.example.geetsunam.features.presentation.login.viewmodel.LoginViewModel
import com.example.geetsunam.features.presentation.signup.SignupActivity
import com.example.geetsunam.features.presentation.signup.viewmodel.SignupEvent
import com.example.geetsunam.features.presentation.signup.viewmodel.SignupState
import com.example.geetsunam.utils.CustomDialog
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.LocalController
import com.example.geetsunam.utils.LogTag
import com.example.geetsunam.utils.Validation
import com.example.geetsunam.utils.models.CommonRequestModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private var oneTapClient: SignInClient? = null
    private var signUpRequest: BeginSignInRequest? = null

    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var googleLoginViewModel: GoogleLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //one tap builders
        oneTapClient = Identity.getSignInClient(this)
        signUpRequest = BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                // Server's client ID, not Android client ID.
                .setServerClientId(getString(R.string.web_client_id))
                // Show all accounts on the device.
                .setFilterByAuthorizedAccounts(false).build()
        ).build()
        binding.btnLogin.setOnClickListener {
            LocalController().unfocusKeyboard(this)
            loginViewModel.onEvent(LoginEvent.CheckValidation)
        }
        binding.tvForgotPassword.setOnClickListener {
            //navigate to forgot password activity
            val forgotPasswordIntent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(forgotPasswordIntent)
        }
        binding.btnSignup.setOnClickListener {
            val signupIntent = Intent(this, SignupActivity::class.java)
            startActivity(signupIntent)
        }
        binding.ibGoogle.setOnClickListener {
            oneTapClient!!.signOut()
            displaySignUp()
        }
        watchTextChange(binding.etLoginEmail, 0)
        watchTextChange(binding.etLoginPassword, 1)
        observeNormalLogin()
        observeGoogleLogin()
    }

    private fun watchTextChange(editText: EditText, index: Int) {
        editText.doOnTextChanged { text, start, before, count ->
            when (index) {
                0 -> {
                    val validationResult = Validation.validateEmail(text.toString())
                    binding.tvLoginEmailError.text = validationResult.message
                    loginViewModel.onEvent(
                        LoginEvent.EmailChanged(
                            validationResult, text.toString()
                        )
                    )
                }

                1 -> {
                    val validationResult = Validation.validatePassword(text.toString())
                    binding.tvLoginPasswordError.text = validationResult.message
                    loginViewModel.onEvent(
                        LoginEvent.PasswordChanged(
                            validationResult, text.toString()
                        )
                    )
                }
            }
        }
    }

    private fun observeNormalLogin() {
        val dialog = Dialog(this)
        loginViewModel.loginState.observe(this) { response ->
            if (response.status == LoginState.LoginStatus.FormValid) {
                loginViewModel.onEvent(LoginEvent.LoginUser)
            }
            if (response.status == LoginState.LoginStatus.FormInvalid) {
                CustomToast.showToast(this, "${response.message}")
            }
            if (response.status == LoginState.LoginStatus.LOADING) {
                //show loading dialog
                CustomDialog().showLoadingDialog(dialog)
            }
            if (response.status == LoginState.LoginStatus.SUCCESS) {
                CustomDialog().hideLoadingDialog(dialog)
                CustomToast.showToast(
                    context = this, "${
                        response.message
                    }"
                )
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
            if (response.status == LoginState.LoginStatus.FAILED) {
                CustomDialog().hideLoadingDialog(dialog)
                CustomToast.showToast(
                    context = this, "${
                        response.message
                    }"
                )
            }
        }
    }

    private fun observeGoogleLogin() {
        val dialog = Dialog(this)
        googleLoginViewModel.googleLoginState.observe(this) { response ->
            if (response.status == GoogleLoginState.GoogleLoginStatus.LOADING) {
                //show loading dialog
                CustomDialog().showLoadingDialog(dialog)
            }
            if (response.status == GoogleLoginState.GoogleLoginStatus.SUCCESS) {
                CustomDialog().hideLoadingDialog(dialog)
                CustomToast.showToast(
                    context = this, "${
                        response.message
                    }"
                )
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
            if (response.status == GoogleLoginState.GoogleLoginStatus.FAILED) {
                CustomDialog().hideLoadingDialog(dialog)
                CustomToast.showToast(
                    context = this, "${
                        response.message
                    }"
                )
            }
        }
    }

    //google onetap
    private val oneTapResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            try {
                val credential = oneTapClient?.getSignInCredentialFromIntent(result.data)
                val idToken = credential?.googleIdToken
                when {
                    idToken != null -> {
                        Log.d(LogTag.GOOGLE, "$idToken")
                        //authenticate through sever
                        googleLoginViewModel.onEvent(
                            GoogleLoginEvent.LoginWithGoogle(
                                CommonRequestModel(googleAccessToken = idToken)
                            )
                        )
                    }

                    else -> {
                        Log.d(LogTag.GOOGLE, "No ID token!")
                        googleLoginViewModel.onEvent(
                            GoogleLoginEvent.HandleError(
                                "Authentication failed,Try Again"
                            )
                        )
                    }
                }
            } catch (e: ApiException) {
                when (e.statusCode) {
                    CommonStatusCodes.CANCELED -> {
                        Log.d(LogTag.GOOGLE, "One-tap dialog was closed.")
                        googleLoginViewModel.onEvent(
                            GoogleLoginEvent.HandleError("Login Cancelled")
                        )
                    }

                    CommonStatusCodes.NETWORK_ERROR -> {
                        Log.d(LogTag.GOOGLE, "One-tap encountered a network error.")
                        googleLoginViewModel.onEvent(
                            GoogleLoginEvent.HandleError(
                                "Please check your internet connection"
                            )
                        )
                    }

                    else -> {
                        Log.d(
                            LogTag.GOOGLE,
                            "Couldn't get credential from result." + " (${e.localizedMessage})"
                        )
                        googleLoginViewModel.onEvent(
                            GoogleLoginEvent.HandleError(
                                "Authentication failed,Try Again"
                            )
                        )
                    }
                }
            }
        }

    private fun displaySignUp() {
        oneTapClient?.beginSignIn(signUpRequest!!)?.addOnSuccessListener(this) { result ->
            try {
                val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                oneTapResult.launch(ib)
            } catch (e: IntentSender.SendIntentException) {
                Log.e(LogTag.GOOGLE, "Couldn't start One Tap UI: ${e.localizedMessage}")
                googleLoginViewModel.onEvent(
                    GoogleLoginEvent.HandleError(
                        e.localizedMessage ?: "Authentication failed,Try Again"
                    )
                )
            }
        }?.addOnFailureListener(this) { e ->
            // No Google Accounts found. Just continue presenting the signed-out UI.
            Log.d(LogTag.GOOGLE, e.localizedMessage!!)
            googleLoginViewModel.onEvent(
                GoogleLoginEvent.HandleError(
                    e.localizedMessage ?: "Authentication failed,Try Again"
                )
            )
        }
    }
}