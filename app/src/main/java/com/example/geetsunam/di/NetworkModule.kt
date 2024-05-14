package com.example.geetsunam.di

import com.example.geetsunam.core.configs.ApiConfig
import com.example.geetsunam.services.network.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
//import okhttp3.logging.HttpLoggingInterceptor
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        // Create an instance of HttpLoggingInterceptor
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY) // Set desired log level

        val client = OkHttpClient.Builder().apply {
            this.connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
        }.build()

        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
            .baseUrl(ApiConfig.baseUrl).build()
    }

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesGson(): Gson = GsonBuilder().create()
}