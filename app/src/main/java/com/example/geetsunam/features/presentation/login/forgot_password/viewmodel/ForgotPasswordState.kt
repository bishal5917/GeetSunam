package com.example.geetsunam.features.presentation.login.forgot_password.viewmodel

data class ForgotPasswordState(
    val status: ForgotPasswordStatus,
    val message: String? = null,
    val email: String? = null,
    val isEmailValid: Boolean = false,
) {
    companion object {
        val idle = ForgotPasswordState(ForgotPasswordStatus.IDLE, message = "")
    }

    enum class ForgotPasswordStatus {
        IDLE, LOADING, SUCCESS, FAILED, FieldChanging, FormValid, FormInvalid
    }
}