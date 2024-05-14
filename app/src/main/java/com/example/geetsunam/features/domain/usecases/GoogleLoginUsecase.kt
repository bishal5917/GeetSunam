package com.example.geetsunam.features.domain.usecases

import android.util.Log
import com.example.geetsunam.features.data.models.login.LoginResponseModel
import com.example.geetsunam.features.domain.repositories.UserRepository
import com.example.geetsunam.utils.Constants
import com.example.geetsunam.utils.LogTag
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GoogleLoginUsecase(private val userRepository: UserRepository) {
    fun call(commonRequestModel: CommonRequestModel): Flow<Resource<LoginResponseModel>> = flow {
        emit(Resource.Loading())
        try {
            val response = userRepository.loginWithGoogle(commonRequestModel)
            Log.d(LogTag.LOGIN, "API Response, ${response.message}")
            Log.d(LogTag.LOGIN, "API Response, ${response.data}")
            emit(response)
        } catch (e: HttpException) {
            Log.d(LogTag.LOGIN, e.localizedMessage!!)
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            Log.d(LogTag.LOGIN, e.localizedMessage!!)
            emit(
                Resource.Error(
                    e.localizedMessage ?: Constants.noInternet
                )
            )
        }
    }
}