package com.example.heartconnect.features.presentation.screens.splash.viewmodel

import com.example.geetsunam.features.domain.entities.UserEntity

sealed class SplashEvent {

    object CheckStatus : SplashEvent()
    data class SetUser(val user: UserEntity) : SplashEvent()
}