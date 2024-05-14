package com.example.geetsunam.features.domain.usecases

import android.util.Log
import com.example.geetsunam.features.data.models.login.LoginRequestModel
import com.example.geetsunam.features.data.models.login.LoginResponseModel
import com.example.geetsunam.features.data.models.songs.SingleSongResponseModel
import com.example.geetsunam.features.domain.repositories.UserRepository
import com.example.geetsunam.utils.Constants
import com.example.geetsunam.utils.LogTag
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class ToggleFavouriteUsecase(private val userRepository: UserRepository) {
    fun call(commonRequestModel: CommonRequestModel): Flow<Resource<SingleSongResponseModel>> =
        flow {
            emit(Resource.Loading())
            try {
                val response = userRepository.toggleFavourite(commonRequestModel)
                Log.d(LogTag.TOGGLEFAV, "API Response, ${response.message}")
                Log.d(LogTag.TOGGLEFAV, "API Response, ${response.data}")
                emit(response)
            } catch (e: HttpException) {
                Log.d(LogTag.TOGGLEFAV, e.localizedMessage!!)
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            } catch (e: IOException) {
                Log.d(LogTag.TOGGLEFAV, e.localizedMessage!!)
                emit(
                    Resource.Error(
                        e.localizedMessage ?: Constants.noInternet
                    )
                )
            }
        }
}