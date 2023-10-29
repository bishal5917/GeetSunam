package com.example.geetsunam.utils.models


import com.google.gson.annotations.SerializedName

data class CommonResponseModel(
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: String?
)