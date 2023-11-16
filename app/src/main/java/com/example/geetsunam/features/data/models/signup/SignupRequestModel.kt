package com.example.geetsunam.features.data.models.signup

data class SignupRequestModel(
    val email: String,
    val password: String,
    val fullname: String,
    val confirmPassword: String,
    val role: String = "user",
)