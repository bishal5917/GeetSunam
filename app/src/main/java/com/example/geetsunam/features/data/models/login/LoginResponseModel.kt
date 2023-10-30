package com.example.geetsunam.features.data.models.login

import com.google.gson.annotations.SerializedName

data class LoginResponseModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("token")
    val token: String?
) {
    data class Data(
        @SerializedName("user")
        val user: User?
    ) {
        data class User(
            @SerializedName("email")
            val email: String?,
            @SerializedName("fullname")
            val fullname: String?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("isFeatured")
            val isFeatured: Boolean?,
            @SerializedName("profileImage")
            val profileImage: String?,
            @SerializedName("role")
            val role: String?
        )
    }
}