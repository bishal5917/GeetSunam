package com.example.geetsunam.features.domain.usecases

import android.util.Log
import com.example.geetsunam.features.data.models.login.LoginRequestModel
import com.example.geetsunam.features.data.models.login.LoginResponseModel
import com.example.geetsunam.features.domain.repositories.UserRepository
import com.example.geetsunam.utils.Constants
import com.example.geetsunam.utils.LogTag
import com.example.geetsunam.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class LoginUsecase(private val userRepository: UserRepository) {
    fun call(loginRequestModel: LoginRequestModel): Flow<Resource<LoginResponseModel>> = flow {
        emit(Resource.Loading())
        try {
            val response = userRepository.login(loginRequestModel)
            emit(response)
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: Constants.noInternet
                )
            )
        }
    }
}