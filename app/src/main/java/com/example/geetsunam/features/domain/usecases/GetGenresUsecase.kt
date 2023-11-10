package com.example.geetsunam.features.domain.usecases

import android.util.Log
import com.example.geetsunam.features.data.models.genres.GenreResponseModel
import com.example.geetsunam.features.domain.entities.GenreEntity
import com.example.geetsunam.features.domain.repositories.UserRepository
import com.example.geetsunam.utils.LogTag
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetGenresUsecase(private val userRepository: UserRepository) {
    fun call(commonRequestModel: CommonRequestModel): Flow<Resource<GenreResponseModel>> = flow {
        emit(Resource.Loading())
        try {
            val response = userRepository.getGenres(commonRequestModel)
            Log.d(LogTag.GENRE, "API Response, ${response.message}")
            Log.d(LogTag.GENRE, "API Response, ${response.data}")
            emit(response)
        } catch (e: HttpException) {
            Log.d(LogTag.GENRE, e.localizedMessage!!)
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            Log.d(LogTag.GENRE, e.localizedMessage!!)
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}