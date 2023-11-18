package com.example.geetsunam.features.presentation.login.forgot_password.viewmodel

import com.example.geetsunam.utils.ValidationResult

sealed class ForgotPasswordEvent {
    object SendResetLink : ForgotPasswordEvent()

    data class EmailChanged(
        val validationResult: ValidationResult, val fieldValue: String
    ) : ForgotPasswordEvent()

    object CheckValidation : ForgotPasswordEvent()
    object Reset : ForgotPasswordEvent()
}