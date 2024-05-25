package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.features.domain.repositories.UserRepository
import com.example.geetsunam.utils.Constants
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import com.example.geetsunam.utils.models.CommonResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class ChangePasswordUsecase(private val userRepository: UserRepository) {
    fun call(commonRequestModel: CommonRequestModel): Flow<Resource<CommonResponseModel>> = flow {
        emit(Resource.Loading())
        try {
            val response = userRepository.changePassword(commonRequestModel)
            emit(response)
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error(Constants.noInternet))
        }
    }
}