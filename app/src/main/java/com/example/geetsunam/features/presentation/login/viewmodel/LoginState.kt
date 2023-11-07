package com.example.geetsunam.features.presentation.login.viewmodel

import com.example.geetsunam.features.data.models.login.LoginResponseModel

data class LoginState(
    val status: LoginStatus,
    val message: String? = null,
    val user: LoginResponseModel.Data.User? = null
) {
    companion object {
        val idle = LoginState(LoginStatus.IDLE, message = "")
    }

    enum class LoginStatus {
        IDLE, LOADING, SUCCESS, FAILED, LogoutLoading, LogoutSuccess, LogoutFailed
    }
}