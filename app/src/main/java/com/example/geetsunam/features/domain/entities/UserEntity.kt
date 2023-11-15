package com.example.geetsunam.features.domain.entities

data class UserEntity(
    val token: String? = null,
    val name: String? = null,
    val email: String? = null,
    val image: String? = null,
    val isGoogleLogin: Boolean? = false
)