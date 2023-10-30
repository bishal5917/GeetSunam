package com.example.geetsunam.features.data.models.login

import com.google.gson.Gson

data class LoginRequestModel (
    val email: String,
    val password: String
)

fun LoginRequestModel.toJson(): String {
    return Gson().toJson(this)
}
