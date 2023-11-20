package com.example.geetsunam.features.presentation.home.change_password.viewmodel

import com.example.geetsunam.utils.ValidationResult

sealed class ChangePasswordEvent {
    data class ChangePassword(val token: String) : ChangePasswordEvent()
    data class PasswordChanged(
        val validationResult: ValidationResult, val fieldValue: String
    ) : ChangePasswordEvent()

    data class NewPasswordChanged(
        val validationResult: ValidationResult, val fieldValue: String
    ) : ChangePasswordEvent()

    data class ConfirmNewPasswordChanged(
        val validationResult: ValidationResult, val fieldValue: String
    ) : ChangePasswordEvent()

    object CheckValidation : ChangePasswordEvent()
    object Reset : ChangePasswordEvent()
}