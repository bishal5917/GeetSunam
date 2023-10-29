package com.example.geetsunam.features.data.datasource

import com.example.geetsunam.features.data.models.login.LoginRequestModel
import com.example.geetsunam.features.data.models.login.LoginResponseModel
import com.example.geetsunam.services.network.ApiService
import retrofit2.Response
import javax.inject.Inject

interface UserRemoteDatasource {
    suspend fun login(loginRequestModel: LoginRequestModel): Response<LoginResponseModel>
}

class UserRemoteDatasourceImpl @Inject constructor(private val apiService: ApiService) :
    UserRemoteDatasource {
    override suspend fun login(loginRequestModel: LoginRequestModel): Response<LoginResponseModel> {
        return apiService.login(
            body = loginRequestModel
        )
    }
}