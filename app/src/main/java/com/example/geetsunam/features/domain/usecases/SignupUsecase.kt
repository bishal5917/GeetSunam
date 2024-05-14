package com.example.geetsunam.features.domain.usecases

import android.util.Log
import com.example.geetsunam.features.data.models.signup.SignupRequestModel
import com.example.geetsunam.features.domain.repositories.UserRepository
import com.example.geetsunam.utils.Constants
import com.example.geetsunam.utils.LogTag
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class SignupUsecase(private val userRepository: UserRepository) {
    fun call(signupRequestModel: SignupRequestModel): Flow<Resource<CommonResponseModel>> = flow {
        emit(Resource.Loading())
        try {
            val response = userRepository.signUp(signupRequestModel)
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