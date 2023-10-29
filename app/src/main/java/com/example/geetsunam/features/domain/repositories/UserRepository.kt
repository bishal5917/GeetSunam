package com.example.geetsunam.features.domain.repositories

import com.example.geetsunam.features.data.models.login.LoginRequestModel
import com.example.geetsunam.features.data.models.login.LoginResponseModel
import com.example.geetsunam.utils.Resource

interface UserRepository {
    suspend fun login(loginRequestModel: LoginRequestModel): Resource<LoginResponseModel>
}