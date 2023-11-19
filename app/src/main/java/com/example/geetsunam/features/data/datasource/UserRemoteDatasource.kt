package com.example.geetsunam.features.data.datasource

import com.example.geetsunam.features.data.models.artist.ArtistResponseModel
import com.example.geetsunam.features.data.models.genres.GenreResponseModel
import com.example.geetsunam.features.data.models.login.LoginRequestModel
import com.example.geetsunam.features.data.models.login.LoginResponseModel
import com.example.geetsunam.features.data.models.search.SearchResponseModel
import com.example.geetsunam.features.data.models.signup.SignupRequestModel
import com.example.geetsunam.features.data.models.songs.RecommendedSongResponseModel
import com.example.geetsunam.features.data.models.songs.SingleSongResponseModel
import com.example.geetsunam.features.data.models.songs.SongResponseModel
import com.example.geetsunam.services.network.ApiService
import com.example.geetsunam.utils.GetQuery
import com.example.geetsunam.utils.models.CommonRequestModel
import com.example.geetsunam.utils.models.CommonResponseModel
import com.example.geetsunam.utils.models.QueryRequestModel
import retrofit2.Response
import javax.inject.Inject

interface UserRemoteDatasource {
    suspend fun login(loginRequestModel: LoginRequestModel): Response<LoginResponseModel>
    suspend fun forgotPassword(commonRequestModel: CommonRequestModel): Response<CommonResponseModel>

    suspend fun signUp(signupRequestModel: SignupRequestModel): Response<CommonResponseModel>

    suspend fun loginWithGoogle(commonRequestModel: CommonRequestModel): Response<LoginResponseModel>

    suspend fun getGenres(commonRequestModel: CommonRequestModel): Response<GenreResponseModel>
    suspend fun getFeaturedArtists(commonRequestModel: CommonRequestModel): Response<ArtistResponseModel>
    suspend fun getFeaturedSongs(commonRequestModel: CommonRequestModel): Response<SongResponseModel>

    suspend fun getTrendingSongs(commonRequestModel: CommonRequestModel): Response<SongResponseModel>

    suspend fun getSingleSong(commonRequestModel: CommonRequestModel): Response<SingleSongResponseModel>

    suspend fun getNewSongs(commonRequestModel: CommonRequestModel): Response<SongResponseModel>

    suspend fun toggleFavourite(commonRequestModel: CommonRequestModel): Response<SingleSongResponseModel>

    suspend fun getFavouriteSongs(commonRequestModel: CommonRequestModel): Response<SongResponseModel>

    suspend fun getGenreSongs(commonRequestModel: CommonRequestModel): Response<SongResponseModel>

    suspend fun getArtistSongs(commonRequestModel: CommonRequestModel): Response<SongResponseModel>

    suspend fun search(queryRequestModel: QueryRequestModel): Response<SearchResponseModel>

    suspend fun getRecommendedSongs(commonRequestModel: CommonRequestModel):
            Response<RecommendedSongResponseModel>

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

    override suspend fun forgotPassword(commonRequestModel: CommonRequestModel): Response<CommonResponseModel> {
        return apiService.forgotPassword(
            body = mapOf<String, String>(
                "email" to commonRequestModel.email!!,
            )
        )
    }

    override suspend fun signUp(signupRequestModel: SignupRequestModel): Response<CommonResponseModel> {
        return apiService.signUp(
            body = mapOf<String, String>(
                "email" to signupRequestModel.email,
                "password" to signupRequestModel.password,
                "fullname" to signupRequestModel.fullname,
                "confirmPassword" to signupRequestModel.confirmPassword,
                "role" to signupRequestModel.role,
            )
        )
    }

    override suspend fun loginWithGoogle(commonRequestModel: CommonRequestModel): Response<LoginResponseModel> {
        return apiService.loginWithGoogle(
            body = mapOf<String, String>(
                "googleAccessToken" to commonRequestModel.googleAccessToken!!,
                "type" to "manual",
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

    override suspend fun getNewSongs(commonRequestModel: CommonRequestModel): Response<SongResponseModel> {
        return apiService.getNewSongs(authToken = "Bearer ${commonRequestModel.token}")
    }

    override suspend fun toggleFavourite(commonRequestModel: CommonRequestModel): Response<SingleSongResponseModel> {
        return apiService.toggleFavourites(
            authToken = "Bearer ${commonRequestModel.token}", body = mapOf<String, String>(
                "songs" to commonRequestModel.songId!!,
            )
        )
    }

    override suspend fun getFavouriteSongs(commonRequestModel: CommonRequestModel): Response<SongResponseModel> {
        return apiService.getFavouriteSongs(authToken = "Bearer ${commonRequestModel.token}")
    }

    override suspend fun getGenreSongs(commonRequestModel: CommonRequestModel): Response<SongResponseModel> {
        return apiService.getGenreSongs(
            authToken = "Bearer ${commonRequestModel.token}",
            genreId = commonRequestModel.genreId ?: "abc"
        )
    }

    override suspend fun getArtistSongs(commonRequestModel: CommonRequestModel): Response<SongResponseModel> {
        return apiService.getArtistSongs(
            authToken = "Bearer ${commonRequestModel.token}",
            genreId = commonRequestModel.artistId ?: "abc"
        )
    }

    override suspend fun search(queryRequestModel: QueryRequestModel): Response<SearchResponseModel> {
        return apiService.search(
            authToken = "Bearer ${queryRequestModel.token}",
            queryMap = GetQuery.getQueryMap(queryRequestModel)
        )
    }

    override suspend fun getRecommendedSongs(commonRequestModel: CommonRequestModel): Response<RecommendedSongResponseModel> {
        return apiService.getRecommendedSongs(
            authToken = "Bearer ${commonRequestModel.token}",
        )
    }
}