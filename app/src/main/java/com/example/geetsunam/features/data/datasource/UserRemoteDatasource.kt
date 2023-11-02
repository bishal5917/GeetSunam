package com.example.geetsunam.features.data.datasource

import com.example.geetsunam.features.data.models.artist.ArtistResponseModel
import com.example.geetsunam.features.data.models.genres.GenreResponseModel
import com.example.geetsunam.features.data.models.login.LoginRequestModel
import com.example.geetsunam.features.data.models.login.LoginResponseModel
import com.example.geetsunam.features.data.models.login.toJson
import com.example.geetsunam.features.data.models.songs.SingleSongResponseModel
import com.example.geetsunam.features.data.models.songs.SongResponseModel
import com.example.geetsunam.services.network.ApiService
import com.example.geetsunam.utils.models.CommonRequestModel
import retrofit2.Response
import javax.inject.Inject

interface UserRemoteDatasource {
    suspend fun login(loginRequestModel: LoginRequestModel): Response<LoginResponseModel>
    suspend fun getGenres(commonRequestModel: CommonRequestModel): Response<GenreResponseModel>
    suspend fun getFeaturedArtists(commonRequestModel: CommonRequestModel): Response<ArtistResponseModel>
    suspend fun getFeaturedSongs(commonRequestModel: CommonRequestModel): Response<SongResponseModel>

    suspend fun getTrendingSongs(commonRequestModel: CommonRequestModel): Response<SongResponseModel>

    suspend fun getSingleSong(commonRequestModel: CommonRequestModel): Response<SingleSongResponseModel>
}

class UserRemoteDatasourceImpl @Inject constructor(private val apiService: ApiService) :
    UserRemoteDatasource {
    override suspend fun login(loginRequestModel: LoginRequestModel): Response<LoginResponseModel> {
        return apiService.login(
            body = mapOf<String, String>(
                "email" to loginRequestModel.email,
                "password" to loginRequestModel.password,
            )
        )
    }

    override suspend fun getGenres(commonRequestModel: CommonRequestModel): Response<GenreResponseModel> {
        return apiService.getGenres(authToken = "Bearer ${commonRequestModel.token}")
    }

    override suspend fun getFeaturedArtists(commonRequestModel: CommonRequestModel): Response<ArtistResponseModel> {
        return apiService.getFeaturedArtists(authToken = "Bearer ${commonRequestModel.token}")
    }

    override suspend fun getFeaturedSongs(commonRequestModel: CommonRequestModel): Response<SongResponseModel> {
        return apiService.getFeaturedSongs(authToken = "Bearer ${commonRequestModel.token}")
    }

    override suspend fun getTrendingSongs(commonRequestModel: CommonRequestModel): Response<SongResponseModel> {
        return apiService.getTrendingSongs(authToken = "Bearer ${commonRequestModel.token}")
    }

    override suspend fun getSingleSong(commonRequestModel: CommonRequestModel): Response<SingleSongResponseModel> {
        return apiService.getSingleSong(
            authToken = "Bearer ${commonRequestModel.token}",
            songId = commonRequestModel.songId ?: "abc"
        )
    }
}