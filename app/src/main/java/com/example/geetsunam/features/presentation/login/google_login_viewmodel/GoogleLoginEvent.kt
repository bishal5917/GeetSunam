package com.example.geetsunam.features.presentation.login.google_login_viewmodel

import com.example.geetsunam.utils.models.CommonRequestModel

sealed class GoogleLoginEvent {
    data class LoginWithGoogle(val commonRequestModel: CommonRequestModel) : GoogleLoginEvent()
    data class HandleError(val message: String) : GoogleLoginEvent()
}