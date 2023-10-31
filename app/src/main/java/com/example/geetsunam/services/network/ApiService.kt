package com.example.geetsunam.services.network

import com.example.geetsunam.features.data.models.artist.ArtistResponseModel
import com.example.geetsunam.features.data.models.genres.GenreResponseModel
import com.example.geetsunam.features.data.models.login.LoginResponseModel
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
}