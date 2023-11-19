package com.example.geetsunam.features.data.repositories

import android.content.res.Resources
import com.example.geetsunam.R
import com.example.geetsunam.features.data.datasource.UserRemoteDatasource
import com.example.geetsunam.features.data.models.artist.ArtistResponseModel
import com.example.geetsunam.features.data.models.genres.GenreResponseModel
import com.example.geetsunam.features.data.models.login.LoginRequestModel
import com.example.geetsunam.features.data.models.login.LoginResponseModel
import com.example.geetsunam.features.data.models.search.SearchResponseModel
import com.example.geetsunam.features.data.models.signup.SignupRequestModel
import com.example.geetsunam.features.data.models.songs.RecommendedSongResponseModel
import com.example.geetsunam.features.data.models.songs.SingleSongResponseModel
import com.example.geetsunam.features.data.models.songs.SongResponseModel
import com.example.geetsunam.features.domain.repositories.UserRepository
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import com.example.geetsunam.utils.models.CommonResponseModel
import com.example.geetsunam.utils.models.QueryRequestModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDatasource: UserRemoteDatasource,
    private val gson: Gson,
    private val resources: Resources
) : UserRepository {
    override suspend fun login(loginRequestModel: LoginRequestModel): Resource<LoginResponseModel> {
        val response = userRemoteDatasource.login(loginRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun forgotPassword(commonRequestModel: CommonRequestModel): Resource<CommonResponseModel> {
        val response = userRemoteDatasource.forgotPassword(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun signUp(signupRequestModel: SignupRequestModel): Resource<CommonResponseModel> {
        val response = userRemoteDatasource.signUp(signupRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun loginWithGoogle(commonRequestModel: CommonRequestModel): Resource<LoginResponseModel> {
        val response = userRemoteDatasource.loginWithGoogle(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun getGenres(commonRequestModel: CommonRequestModel): Resource<GenreResponseModel> {
        val response = userRemoteDatasource.getGenres(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun getFeaturedArtists(commonRequestModel: CommonRequestModel): Resource<ArtistResponseModel> {
        val response = userRemoteDatasource.getFeaturedArtists(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun getFeaturedSongs(commonRequestModel: CommonRequestModel): Resource<SongResponseModel> {
        val response = userRemoteDatasource.getFeaturedSongs(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun getTrendingSongs(commonRequestModel: CommonRequestModel): Resource<SongResponseModel> {
        val response = userRemoteDatasource.getTrendingSongs(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun getSingleSong(commonRequestModel: CommonRequestModel): Resource<SingleSongResponseModel> {
        val response = userRemoteDatasource.getSingleSong(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun getNewSongs(commonRequestModel: CommonRequestModel): Resource<SongResponseModel> {
        val response = userRemoteDatasource.getNewSongs(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun toggleFavourite(commonRequestModel: CommonRequestModel): Resource<SingleSongResponseModel> {
        val response = userRemoteDatasource.toggleFavourite(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun getFavouriteSongs(commonRequestModel: CommonRequestModel): Resource<SongResponseModel> {
        val response = userRemoteDatasource.getFavouriteSongs(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun getGenreSongs(commonRequestModel: CommonRequestModel): Resource<SongResponseModel> {
        val response = userRemoteDatasource.getGenreSongs(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun getArtistSongs(commonRequestModel: CommonRequestModel): Resource<SongResponseModel> {
        val response = userRemoteDatasource.getArtistSongs(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun search(queryRequestModel: QueryRequestModel): Resource<SearchResponseModel> {
        val response = userRemoteDatasource.search(queryRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun getRecommendedSongs(commonRequestModel: CommonRequestModel): Resource<RecommendedSongResponseModel> {
        val response = userRemoteDatasource.getRecommendedSongs(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }

    override suspend fun trackPlayedSong(commonRequestModel: CommonRequestModel): Resource<CommonResponseModel> {
        val response = userRemoteDatasource.trackPlayedSong(commonRequestModel)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(CommonResponseModel(message = "Success"))
            }
        }
        //handle error response
        val errorBodyString = response.errorBody()?.string()
        errorBodyString?.let {
            try {
                val errorData = gson.fromJson(it, CommonResponseModel::class.java)
                return Resource.Error(
                    message = errorData?.message ?: resources.getString(R.string.some_error)
                )
            } catch (e: JsonSyntaxException) {
                return Resource.Error(message = resources.getString(R.string.parse_error))
            }
        }
        return Resource.Error(message = resources.getString(R.string.some_error))
    }
}