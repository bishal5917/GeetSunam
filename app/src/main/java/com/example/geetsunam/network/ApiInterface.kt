package com.example.geetsunam.network

import com.example.geetsunam.features.data.models.artist.ArtistResponseModel
import com.example.geetsunam.features.data.models.genres.GenreResponseModel
import com.example.geetsunam.features.data.models.login.LoginResponseModel
import com.example.geetsunam.features.data.models.search.SearchResponseModel
import com.example.geetsunam.features.data.models.songs.RecommendedSongResponseModel
import com.example.geetsunam.features.data.models.songs.SingleSongResponseModel
import com.example.geetsunam.features.data.models.songs.SongResponseModel
import com.example.geetsunam.utils.models.CommonResponseModel
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    //login
    @POST("users/login")
    @JvmSuppressWildcards
    suspend fun login(
        @Body body: Any
    ): Response<LoginResponseModel>

    @POST("users/forget-password")
    @JvmSuppressWildcards
    suspend fun forgotPassword(
        @Body body: Any
    ): Response<CommonResponseModel>

    @POST("users/signup")
    @JvmSuppressWildcards
    suspend fun signUp(
        @Body body: Any
    ): Response<CommonResponseModel>

    //google login
    @POST("users/google")
    @JvmSuppressWildcards
    suspend fun loginWithGoogle(
        @Body body: Any
    ): Response<LoginResponseModel>

    //get genres
    @GET("genre")
    @JvmSuppressWildcards
    suspend fun getGenres(
        @Header("Authorization") authToken: String,
    ): Response<GenreResponseModel>

    //get featured artists
    @GET("artists/featured")
    @JvmSuppressWildcards
    suspend fun getFeaturedArtists(
        @Header("Authorization") authToken: String,
    ): Response<ArtistResponseModel>

    //get featured songs
    @GET("songs/featured")
    @JvmSuppressWildcards
    suspend fun getFeaturedSongs(
        @Header("Authorization") authToken: String,
    ): Response<SongResponseModel>

    //get trending songs
    @GET("songs/trending")
    @JvmSuppressWildcards
    suspend fun getTrendingSongs(
        @Header("Authorization") authToken: String,
    ): Response<SongResponseModel>

    // Get single song
    @GET("songs/{id}")
    @JvmSuppressWildcards
    suspend fun getSingleSong(
        @Header("Authorization") authToken: String, @Path("id") songId: String
    ): Response<SingleSongResponseModel>

    //get newly released songs
    @GET("songs/new-releases")
    @JvmSuppressWildcards
    suspend fun getNewSongs(
        @Header("Authorization") authToken: String,
    ): Response<SongResponseModel>

    // favourite / unfavourite songs
    @PATCH("favourite/songs/toggle")
    @JvmSuppressWildcards
    suspend fun toggleFavourites(
        @Header("Authorization") authToken: String, @Body body: Any
    ): Response<SingleSongResponseModel>

    //get favourite songs
    @GET("favourite/songs")
    @JvmSuppressWildcards
    suspend fun getFavouriteSongs(
        @Header("Authorization") authToken: String,
    ): Response<SongResponseModel>

    //get genre songs
    @GET("songs/genres/{id}")
    @JvmSuppressWildcards
    suspend fun getGenreSongs(
        @Header("Authorization") authToken: String, @Path("id") genreId: String
    ): Response<SongResponseModel>

    //get artists songs
    @GET("songs/artists/{id}")
    @JvmSuppressWildcards
    suspend fun getArtistSongs(
        @Header("Authorization") authToken: String, @Path("id") genreId: String
    ): Response<SongResponseModel>

    //global search
    @GET("search")
    @JvmSuppressWildcards
    suspend fun search(
        @Header("Authorization") authToken: String, @QueryMap queryMap: Map<String, Any>
    ): Response<SearchResponseModel>

    //get favourite songs
    @GET("users/songs/recommend")
    @JvmSuppressWildcards
    suspend fun getRecommendedSongs(
        @Header("Authorization") authToken: String,
    ): Response<RecommendedSongResponseModel>

    //track played song
    @PATCH("users/songs/track")
    @JvmSuppressWildcards
    suspend fun trackPlayedSong(
        @Header("Authorization") authToken: String,
        @Body body: Any
    ): Response<CommonResponseModel>

    //change password
    @PATCH("users/change-password")
    @JvmSuppressWildcards
    suspend fun changePassword(
        @Header("Authorization") authToken: String,
        @Body body: Any
    ): Response<CommonResponseModel>
}