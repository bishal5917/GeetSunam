package com.example.geetsunam.utils.models


import com.google.gson.annotations.SerializedName

data class CommonResponseModel(
    @SerializedName("status") val status: String? = null, @SerializedName("message") val message:
    String?
)