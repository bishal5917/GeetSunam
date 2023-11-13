package com.example.geetsunam.features.domain.usecases

import android.util.Log
import com.example.geetsunam.features.data.models.search.SearchResponseModel
import com.example.geetsunam.features.domain.repositories.UserRepository
import com.example.geetsunam.utils.LogTag
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.QueryRequestModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class SearchUsecase(private val userRepository: UserRepository) {
    fun call(queryRequestModel: QueryRequestModel): Flow<Resource<SearchResponseModel>> =
        flow {
            emit(Resource.Loading())
            try {
                val response = userRepository.search(queryRequestModel)
                Log.d(LogTag.SEARCH, "API Response, ${response.message}")
                Log.d(LogTag.SEARCH, "API Response, ${response.data}")
                emit(response)
            } catch (e: HttpException) {
                Log.d(LogTag.SEARCH, e.localizedMessage!!)
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            } catch (e: IOException) {
                Log.d(LogTag.SEARCH, e.localizedMessage!!)
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            }
        }
}