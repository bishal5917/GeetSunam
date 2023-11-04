package com.example.geetsunam.features.presentation.login

import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.geetsunam.MainActivity
import com.example.geetsunam.R
import com.example.geetsunam.features.presentation.login.viewmodel.LoginEvent
import com.example.geetsunam.features.presentation.login.viewmodel.LoginState
import com.example.geetsunam.features.presentation.login.viewmodel.LoginViewModel
import com.example.geetsunam.features.presentation.signup.SignupActivity
import com.example.geetsunam.utils.CustomDialog
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.LocalController
import com.example.geetsunam.utils.LogTag
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

    @Inject
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //one tap builders
        oneTapClient = Identity.getSignInClient(this)
        signUpRequest = BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                // Server's client ID, not Android client ID.
                .setServerClientId(getString(R.string.web_client_id))
                // Show all accounts on the device.
                .setFilterByAuthorizedAccounts(false).build()
        ).build()
        //
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val forgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        val gotoSignUpBtn = findViewById<Button>(R.id.btnSignup)
        val googleSignInBtn = findViewById<ImageButton>(R.id.ibGoogle)
        loginBtn.setOnClickListener {
            LocalController().unfocusKeyboard(this)
            loginUser()
        }
        forgotPassword.setOnClickListener {
            //navigate to forgot password fragment/activity
        }
        gotoSignUpBtn.setOnClickListener {
            val signupIntent = Intent(this, SignupActivity::class.java)
            startActivity(signupIntent)
        }
        googleSignInBtn.setOnClickListener {
            displaySignUp()
        }
        observeLiveData()
    }

    private fun loginUser() {
        val email = findViewById<EditText>(R.id.etLoginEmail).text.toString()
        val pass = findViewById<EditText>(R.id.etLoginPassword).text.toString()
        if (email.isNotEmpty() && pass.isNotEmpty()) {
            loginViewModel.onEvent(
                LoginEvent.LoginUser(
                    email = email, password = pass
                )
            )
        }
    }

    private fun observeLiveData() {
        val dialog = Dialog(this)
        loginViewModel.loginState.observe(this) { response ->
            if (response != null) {
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
    }

    //google onetap
    private val oneTapResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            try {
                val credential = oneTapClient?.getSignInCredentialFromIntent(result.data)
                val idToken = credential?.googleIdToken
                when {
                    idToken != null -> {
                        val msg = "idToken: $idToken"
                        Log.d(LogTag.GOOGLE, msg)
                    }

                    else -> {
                        Log.d(LogTag.GOOGLE, "No ID token!")
                    }
                }
            } catch (e: ApiException) {
                when (e.statusCode) {
                    CommonStatusCodes.CANCELED -> {
                        Log.d(LogTag.GOOGLE, "One-tap dialog was closed.")
                    }

                    CommonStatusCodes.NETWORK_ERROR -> {
                        Log.d(LogTag.GOOGLE, "One-tap encountered a network error.")
                    }

                    else -> {
                        Log.d(
                            LogTag.GOOGLE,
                            "Couldn't get credential from result." + " (${e.localizedMessage})"
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
            }
        }?.addOnFailureListener(this) { e ->
            // No Google Accounts found. Just continue presenting the signed-out UI.
            Log.d(LogTag.GOOGLE, e.localizedMessage!!)
        }
    }
}