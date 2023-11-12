package com.example.geetsunam.di

import com.example.geetsunam.features.domain.repositories.UserRepository
import com.example.geetsunam.features.domain.usecases.GetArtistSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetFavouriteSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetFeaturedArtistsUsecase
import com.example.geetsunam.features.domain.usecases.GetFeaturedSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetGenreSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetGenresUsecase
import com.example.geetsunam.features.domain.usecases.GetNewSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetSingleSongUsecase
import com.example.geetsunam.features.domain.usecases.GetTrendingSongsUsecase
import com.example.geetsunam.features.domain.usecases.GoogleLoginUsecase
import com.example.geetsunam.features.domain.usecases.LoginUsecase
import com.example.geetsunam.features.domain.usecases.ToggleFavouriteUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsecaseModule {
    @Provides
    fun provideLoginUsecase(repo: UserRepository): LoginUsecase {
        return LoginUsecase(repo)
    }

    @Provides
    fun providegoogleLoginUsecase(repo: UserRepository): GoogleLoginUsecase {
        return GoogleLoginUsecase(repo)
    }

    @Provides
    @Singleton
    fun provideGenreUsecase(repo: UserRepository): GetGenresUsecase {
        return GetGenresUsecase(repo)
    }

    @Provides
    @Singleton
    fun provideFeaturedArtistsUsecase(repo: UserRepository): GetFeaturedArtistsUsecase {
        return GetFeaturedArtistsUsecase(repo)
    }

    @Provides
    @Singleton
    fun provideFeaturedSongsUsecase(repo: UserRepository): GetFeaturedSongsUsecase {
        return GetFeaturedSongsUsecase(repo)
    }

    @Provides
    @Singleton
    fun provideTrendingSongsUsecase(repo: UserRepository): GetTrendingSongsUsecase {
        return GetTrendingSongsUsecase(repo)
    }

    @Provides
    fun provideSingleSongUsecase(repo: UserRepository): GetSingleSongUsecase {
        return GetSingleSongUsecase(repo)
    }

    @Provides
    @Singleton
    fun provideNewSongsUsecase(repo: UserRepository): GetNewSongsUsecase {
        return GetNewSongsUsecase(repo)
    }

    @Provides
    fun provideToggleFavUsecase(repo: UserRepository): ToggleFavouriteUsecase {
        return ToggleFavouriteUsecase(repo)
    }

    @Provides
    @Singleton
    fun favSongsUsecase(repo: UserRepository): GetFavouriteSongsUsecase {
        return GetFavouriteSongsUsecase(repo)
    }

    @Provides
    fun provideGenreSongsUsecase(repo: UserRepository): GetGenreSongsUsecase {
        return GetGenreSongsUsecase(repo)
    }

    @Provides
    fun provideArtistSongsUsecase(repo: UserRepository): GetArtistSongsUsecase {
        return GetArtistSongsUsecase(repo)
    }
}