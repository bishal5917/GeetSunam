package com.example.geetsunam.features.presentation.signup.viewmodel

import com.example.geetsunam.utils.ValidationResult

sealed class SignupEvent {
    data class FullNameChanged(
        val validationResult: ValidationResult, val fieldValue: String
    ) : SignupEvent()

    data class EmailChanged(
        val validationResult: ValidationResult, val fieldValue: String
    ) : SignupEvent()

    data class PasswordChanged(
        val validationResult: ValidationResult, val fieldValue: String
    ) : SignupEvent()

    data class ConfirmPasswordChanged(
        val validationResult: ValidationResult, val fieldValue: String
    ) : SignupEvent()

    object CheckValidation : SignupEvent()
    object Signup : SignupEvent()
    object Reset : SignupEvent()
}