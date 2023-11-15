package com.example.geetsunam.features.presentation.splash.viewmodel

data class SplashState(
    val status: SplashStatus, val message: String? = null, val token: String? = null, val
    isGoogleLoggedIn: Boolean? = false
) {
    companion object {
        val IDLE = SplashState(SplashStatus.IDLE, message = "Idle")
    }

    enum class SplashStatus {
        IDLE, LOADING, LoggedIn, GoogleLoggedIn, LoggedOut
    }
}