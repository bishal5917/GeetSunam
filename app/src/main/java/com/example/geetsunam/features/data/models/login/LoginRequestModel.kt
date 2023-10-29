package com.example.geetsunam.features.data.models.login

import com.google.gson.Gson

data class LoginRequestModel (
    val message: String,
    val status: String
)

fun LoginRequestModel.toJson(): String {
    return Gson().toJson(this)
}
