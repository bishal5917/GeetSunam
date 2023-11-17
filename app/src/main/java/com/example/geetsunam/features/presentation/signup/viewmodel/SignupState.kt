package com.example.geetsunam.features.presentation.signup.viewmodel

data class SignupState(
    val status: SignupStatus,
    val message: String? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val isNameValid: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isConfirmPasswordValid: Boolean = false,
) {
    companion object {
        val idle = SignupState(SignupStatus.IDLE, message = "")
    }

    enum class SignupStatus {
        IDLE, LOADING, SUCCESS, FAILED, FieldChanging, FormValid, FormInvalid
    }
}