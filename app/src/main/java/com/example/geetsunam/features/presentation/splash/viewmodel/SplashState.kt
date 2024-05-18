package com.example.geetsunam.features.presentation.splash.viewmodel

import com.example.geetsunam.features.domain.entities.UserEntity

data class SplashState(
    val status: SplashStatus,
    val message: String? = null,
    val userEntity: UserEntity? = null,
    val isGoogleLoggedIn: Boolean? = false
) {
    companion object {
        val IDLE = SplashState(SplashStatus.IDLE, message = "Idle")
    }

    enum class SplashStatus {
        IDLE, LOADING, LoggedIn, GoogleLoggedIn, SessionExpired, LoggedOut
    }
}