package com.example.geetsunam.features.presentation.login.viewmodel

sealed class LoginEvent {
    data class LoginUser(val email: String, val password: String) : LoginEvent()
}