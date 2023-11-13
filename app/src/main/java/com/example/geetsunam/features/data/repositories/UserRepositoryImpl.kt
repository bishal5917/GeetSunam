package com.example.geetsunam.features.data.repositories

import com.example.geetsunam.features.data.datasource.UserRemoteDatasource
import com.example.geetsunam.features.data.models.artist.ArtistResponseModel
import com.example.geetsunam.features.data.models.genres.GenreResponseModel
import com.example.geetsunam.features.data.models.login.LoginRequestModel
import com.example.geetsunam.features.data.models.login.LoginResponseModel
import com.example.geetsunam.features.data.models.search.SearchResponseModel
import com.example.geetsunam.features.data.models.songs.SingleSongResponseModel
import com.example.geetsunam.features.data.models.songs.SongResponseModel
import com.example.geetsunam.features.domain.repositories.UserRepository
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import com.example.geetsunam.utils.models.QueryRequestModel

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

    override suspend fun loginWithGoogle(commonRequestModel: CommonRequestModel): Resource<LoginResponseModel> {
        val response = userRemoteDatasource.loginWithGoogle(commonRequestModel)
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

    override suspend fun getFeaturedSongs(commonRequestModel: CommonRequestModel): Resource<SongResponseModel> {
        val response = userRemoteDatasource.getFeaturedSongs(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(message = "${response.errorBody()?.string()}")
    }

    override suspend fun getTrendingSongs(commonRequestModel: CommonRequestModel): Resource<SongResponseModel> {
        val response = userRemoteDatasource.getTrendingSongs(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(message = "${response.errorBody()?.string()}")
    }

    override suspend fun getSingleSong(commonRequestModel: CommonRequestModel): Resource<SingleSongResponseModel> {
        val response = userRemoteDatasource.getSingleSong(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(message = "${response.errorBody()?.string()}")
    }

    override suspend fun getNewSongs(commonRequestModel: CommonRequestModel): Resource<SongResponseModel> {
        val response = userRemoteDatasource.getNewSongs(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(message = "${response.errorBody()?.string()}")
    }

    override suspend fun toggleFavourite(commonRequestModel: CommonRequestModel): Resource<SingleSongResponseModel> {
        val response = userRemoteDatasource.toggleFavourite(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(message = "${response.errorBody()?.string()}")
    }

    override suspend fun getFavouriteSongs(commonRequestModel: CommonRequestModel): Resource<SongResponseModel> {
        val response = userRemoteDatasource.getFavouriteSongs(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(message = "${response.errorBody()?.string()}")
    }

    override suspend fun getGenreSongs(commonRequestModel: CommonRequestModel): Resource<SongResponseModel> {
        val response = userRemoteDatasource.getGenreSongs(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(message = "${response.errorBody()?.string()}")
    }

    override suspend fun getArtistSongs(commonRequestModel: CommonRequestModel): Resource<SongResponseModel> {
        val response = userRemoteDatasource.getArtistSongs(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(message = "${response.errorBody()?.string()}")
    }

    override suspend fun search(queryRequestModel: QueryRequestModel): Resource<SearchResponseModel> {
        val response = userRemoteDatasource.search(queryRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(message = "${response.errorBody()?.string()}")
    }
}