package com.example.geetsunam.features.presentation.login.viewmodel

import com.example.geetsunam.features.domain.entities.UserEntity

data class LoginState(
    val status: LoginStatus,
    val message: String? = null,
    val user: UserEntity? = null
) {
    companion object {
        val idle = LoginState(LoginStatus.IDLE, message = "")
    }

    enum class LoginStatus {
        IDLE, LOADING, SUCCESS, FAILED, LogoutLoading, LogoutSuccess, LogoutFailed
    }
}