package com.example.geetsunam.services.network

import com.example.geetsunam.features.data.models.login.LoginResponseModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    //login
    @POST("api/users/login")
    @JvmSuppressWildcards
    suspend fun login(
        @Body body: Any
    ): Response<LoginResponseModel>
}