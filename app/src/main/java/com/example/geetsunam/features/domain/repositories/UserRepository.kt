package com.example.geetsunam.features.domain.repositories

import com.example.geetsunam.features.data.models.artist.ArtistResponseModel
import com.example.geetsunam.features.data.models.genres.GenreResponseModel
import com.example.geetsunam.features.data.models.login.LoginRequestModel
import com.example.geetsunam.features.data.models.login.LoginResponseModel
import com.example.geetsunam.features.data.models.search.SearchResponseModel
import com.example.geetsunam.features.data.models.signup.SignupRequestModel
import com.example.geetsunam.features.data.models.songs.SingleSongResponseModel
import com.example.geetsunam.features.data.models.songs.SongResponseModel
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import com.example.geetsunam.utils.models.CommonResponseModel
import com.example.geetsunam.utils.models.QueryRequestModel
import retrofit2.Response

interface UserRepository {
    suspend fun login(loginRequestModel: LoginRequestModel): Resource<LoginResponseModel>
    suspend fun forgotPassword(commonRequestModel: CommonRequestModel): Resource<CommonResponseModel>

    suspend fun signUp(signupRequestModel: SignupRequestModel): Resource<CommonResponseModel>

    suspend fun loginWithGoogle(commonRequestModel: CommonRequestModel): Resource<LoginResponseModel>

    suspend fun getGenres(commonRequestModel: CommonRequestModel): Resource<GenreResponseModel>
    suspend fun getFeaturedArtists(commonRequestModel: CommonRequestModel):
            Resource<ArtistResponseModel>

    suspend fun getFeaturedSongs(commonRequestModel: CommonRequestModel):
            Resource<SongResponseModel>

    suspend fun getTrendingSongs(commonRequestModel: CommonRequestModel):
            Resource<SongResponseModel>

    suspend fun getSingleSong(commonRequestModel: CommonRequestModel):
            Resource<SingleSongResponseModel>

    suspend fun getNewSongs(commonRequestModel: CommonRequestModel):
            Resource<SongResponseModel>

    suspend fun toggleFavourite(commonRequestModel: CommonRequestModel):
            Resource<SingleSongResponseModel>

    suspend fun getFavouriteSongs(commonRequestModel: CommonRequestModel):
            Resource<SongResponseModel>

    suspend fun getGenreSongs(commonRequestModel: CommonRequestModel):
            Resource<SongResponseModel>

    suspend fun getArtistSongs(commonRequestModel: CommonRequestModel):
            Resource<SongResponseModel>

    suspend fun search(queryRequestModel: QueryRequestModel):
            Resource<SearchResponseModel>

    suspend fun getRecommendedSongs(commonRequestModel: CommonRequestModel):
            Resource<SongResponseModel>
}