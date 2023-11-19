package com.example.geetsunam.features.presentation.login.forgot_password

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.example.geetsunam.databinding.ActivityForgotPasswordBinding
import com.example.geetsunam.features.presentation.login.forgot_password.viewmodel.ForgotPasswordEvent
import com.example.geetsunam.features.presentation.login.forgot_password.viewmodel.ForgotPasswordState
import com.example.geetsunam.features.presentation.login.forgot_password.viewmodel.ForgotPasswordViewModel
import com.example.geetsunam.utils.CustomDialog
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.LocalController
import com.example.geetsunam.utils.Validation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgotPasswordBinding

    @Inject
    lateinit var forgotPasswordViewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        watchTextChange(binding.etForgotEmail, 0)
        binding.btnForgotPasswordSubmit.setOnClickListener {
            LocalController().unfocusKeyboard(this)
            forgotPasswordViewModel.onEvent(ForgotPasswordEvent.CheckValidation)
        }
        observeLiveData()
    }

    private fun watchTextChange(editText: EditText, index: Int) {
        editText.doOnTextChanged { text, start, before, count ->
            when (index) {
                0 -> {
                    val validationResult = Validation.validateEmail(text.toString())
                    binding.tvForgotEmailError.text = validationResult.message
                    forgotPasswordViewModel.onEvent(
                        ForgotPasswordEvent.EmailChanged(
                            validationResult, text.toString()
                        )
                    )
                }
            }
        }
    }

    private fun observeLiveData() {
        val dialog = Dialog(this)
        forgotPasswordViewModel.forgotPasswordState.observe(this) { response ->
            if (response.status == ForgotPasswordState.ForgotPasswordStatus.FormValid) {
                forgotPasswordViewModel.onEvent(ForgotPasswordEvent.SendResetLink)
            }
            if (response.status == ForgotPasswordState.ForgotPasswordStatus.FormInvalid) {
                CustomToast.showToast(this, "${response.message}")
            }
            if (response.status == ForgotPasswordState.ForgotPasswordStatus.LOADING) {
                //show loading dialog
                CustomDialog().showLoadingDialog(dialog)
            }
            if (response.status == ForgotPasswordState.ForgotPasswordStatus.SUCCESS) {
                CustomDialog().hideLoadingDialog(dialog)
                CustomToast.showToast(
                    context = this, "${
                        response.message
                    }"
                )
                finish()
            }
            if (response.status == ForgotPasswordState.ForgotPasswordStatus.FAILED) {
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