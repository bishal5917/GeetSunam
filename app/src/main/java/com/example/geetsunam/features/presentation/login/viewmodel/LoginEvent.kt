package com.example.geetsunam.features.presentation.login.viewmodel

import com.example.geetsunam.utils.ValidationResult

sealed class LoginEvent {
    object LoginUser : LoginEvent()
    object LogoutUser : LoginEvent()
    data class EmailChanged(
        val validationResult: ValidationResult, val fieldValue: String
    ) : LoginEvent()

    data class PasswordChanged(
        val validationResult: ValidationResult, val fieldValue: String
    ) : LoginEvent()

    object CheckValidation : LoginEvent()
    object Reset : LoginEvent()
}