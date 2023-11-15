package com.example.geetsunam.features.presentation.login.google_login_viewmodel

import com.example.geetsunam.features.domain.entities.UserEntity

data class GoogleLoginState(
    val status: GoogleLoginStatus,
    val message: String? = null,
    val user: UserEntity? = null
) {
    companion object {
        val idle = GoogleLoginState(GoogleLoginStatus.IDLE, message = "")
    }

    enum class GoogleLoginStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}