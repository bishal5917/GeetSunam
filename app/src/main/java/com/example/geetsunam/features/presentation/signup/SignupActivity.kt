package com.example.geetsunam.features.presentation.signup

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import androidx.core.widget.doOnTextChanged
import com.example.geetsunam.MainActivity
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivitySignupBinding
import com.example.geetsunam.features.presentation.home.search.viewmodel.SearchEvent
import com.example.geetsunam.features.presentation.login.google_login_viewmodel.GoogleLoginState
import com.example.geetsunam.features.presentation.signup.viewmodel.SignupEvent
import com.example.geetsunam.features.presentation.signup.viewmodel.SignupState
import com.example.geetsunam.features.presentation.signup.viewmodel.SignupViewModel
import com.example.geetsunam.utils.CustomDialog
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.LocalController
import com.example.geetsunam.utils.Validation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    @Inject
    lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //on text changed
        watchTextChange(binding.etFullName, 0)
        watchTextChange(binding.etSignupEmail, 1)
        watchTextChange(binding.etSignupPassword, 2)
        watchTextChange(binding.etConfirmPassword, 3)
        binding.btnSignupSubmit.setOnClickListener {
            LocalController().unfocusKeyboard(this)
            signupViewModel.onEvent(SignupEvent.CheckValidation)
        }
        observeSignup()
    }

    private fun observeSignup() {
        val dialog = Dialog(this)
        signupViewModel.signupState.observe(this) { response ->
            if (response.status == SignupState.SignupStatus.FormValid) {
                signupViewModel.onEvent(SignupEvent.Signup)
            }
            if (response.status == SignupState.SignupStatus.FormInvalid) {
                CustomToast.showToast(this, "${response.message}")
            }
            if (response.status == SignupState.SignupStatus.LOADING) {
                //show loading dialog
                CustomDialog().showLoadingDialog(dialog)
            }
            if (response.status == SignupState.SignupStatus.SUCCESS) {
                CustomDialog().hideLoadingDialog(dialog)
                CustomToast.showToast(
                    context = this, "${
                        response.message
                    }"
                )
                finish()
            }
            if (response.status == SignupState.SignupStatus.FAILED) {
                CustomDialog().hideLoadingDialog(dialog)
                CustomToast.showToast(
                    context = this, "${
                        response.message
                    }"
                )
                signupViewModel.onEvent(SignupEvent.Reset)
            }
        }
    }

    private fun watchTextChange(editText: EditText, index: Int) {
        editText.doOnTextChanged { text, start, before, count ->
            when (index) {
                0 -> {
                    val validationResult = Validation.validateName(text.toString())
                    binding.tvNameError.text = validationResult.message
                    signupViewModel.onEvent(
                        SignupEvent.FullNameChanged(
                            validationResult, text.toString()
                        )
                    )
                }

                1 -> {
                    val validationResult = Validation.validateEmail(text.toString())
                    binding.tvEmailError.text = validationResult.message
                    signupViewModel.onEvent(
                        SignupEvent.EmailChanged(
                            validationResult, text.toString()
                        )
                    )
                }

                2 -> {
                    val validationResult = Validation.validatePassword(text.toString())
                    binding.tvPasswordError.text = validationResult.message
                    signupViewModel.onEvent(
                        SignupEvent.PasswordChanged(
                            validationResult, text.toString()
                        )
                    )
                }

                3 -> {
                    val validationResult = Validation.confirmPassword(
                        binding.etSignupPassword.text.toString(), text.toString()
                    )
                    binding.tvConfirmPasswordError.text = validationResult.message
                    signupViewModel.onEvent(
                        SignupEvent.ConfirmPasswordChanged(
                            validationResult, text.toString()
                        )
                    )
                }
            }
        }
    }
}