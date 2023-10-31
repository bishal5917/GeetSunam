package com.example.geetsunam.features.data.repositories

import com.example.geetsunam.features.data.datasource.UserRemoteDatasource
import com.example.geetsunam.features.data.models.artist.ArtistResponseModel
import com.example.geetsunam.features.data.models.genres.GenreResponseModel
import com.example.geetsunam.features.data.models.login.LoginRequestModel
import com.example.geetsunam.features.data.models.login.LoginResponseModel
import com.example.geetsunam.features.domain.repositories.UserRepository
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel

class UserRepositoryImpl(
    private val userRemoteDatasource: UserRemoteDatasource,
) : UserRepository {
    override suspend fun login(loginRequestModel: LoginRequestModel): Resource<LoginResponseModel> {
        val response = userRemoteDatasource.login(loginRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(message = "${response.errorBody()?.string()}")
    }

    override suspend fun getGenres(commonRequestModel: CommonRequestModel): Resource<GenreResponseModel> {
        val response = userRemoteDatasource.getGenres(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(message = "${response.errorBody()?.string()}")
    }

    override suspend fun getFeaturedArtists(commonRequestModel: CommonRequestModel): Resource<ArtistResponseModel> {
        val response = userRemoteDatasource.getFeaturedArtists(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(message = "${response.errorBody()?.string()}")
    }
}