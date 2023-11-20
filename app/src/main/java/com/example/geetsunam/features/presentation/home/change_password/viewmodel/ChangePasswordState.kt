package com.example.geetsunam.features.presentation.home.change_password.viewmodel

data class ChangePasswordState(
    val status: ChangePasswordStatus,
    val message: String? = null,
    val password: String? = null,
    val newPassword: String? = null,
    val confirmNewPassword: String? = null,
    val isPasswordValid: Boolean = false,
    val isNewPasswordValid: Boolean = false,
    val isConfirmPasswordValid: Boolean = false,
) {
    companion object {
        val idle = ChangePasswordState(ChangePasswordStatus.IDLE, message = "")
    }

    enum class ChangePasswordStatus {
        IDLE, LOADING, SUCCESS, FAILED, FieldChanging, FormValid, FormInvalid
    }
}