package com.example.geetsunam.utils.models

data class CommonRequestModel(
    val token: String? = null,
    val songId: String? = null,
    val genreId: String? = null,
    val artistId: String? = null,
    val googleAccessToken: String? = null,
    val email: String? = null,
    val currentPassword: String? = null,
    val newPassword: String? = null,
    val confirmNewPassword: String? = null
)