package com.example.geetsunam.services.network

import com.example.geetsunam.features.data.models.artist.ArtistResponseModel
import com.example.geetsunam.features.data.models.genres.GenreResponseModel
import com.example.geetsunam.features.data.models.login.LoginResponseModel
import com.example.geetsunam.features.data.models.songs.SingleSongResponseModel
import com.example.geetsunam.features.data.models.songs.SongResponseModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    //login
    @POST("users/login")
    @JvmSuppressWildcards
    suspend fun login(
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

    //get trending songs
    @GET("songs/new-releases")
    @JvmSuppressWildcards
    suspend fun getNewSongs(
        @Header("Authorization") authToken: String,
    ): Response<SongResponseModel>

    // Get single song
    @PATCH("favourite/songs/toggle")
    @JvmSuppressWildcards
    suspend fun toggleFavourites(
        @Header("Authorization") authToken: String, @Body body: Any
    ): Response<SingleSongResponseModel>
}