package com.example.geetsunam.utils.models


import com.google.gson.annotations.SerializedName

data class CommonResponseModel(
    @SerializedName("status") val status: String?, @SerializedName("message") val message: String?
)