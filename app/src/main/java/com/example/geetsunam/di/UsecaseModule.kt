package com.example.geetsunam.di

import com.example.geetsunam.features.data.datasource.UserLocalDatasource
import com.example.geetsunam.features.domain.repositories.UserRepository
import com.example.geetsunam.features.domain.usecases.ChangePasswordUsecase
import com.example.geetsunam.features.domain.usecases.ForgotPasswordUsecase
import com.example.geetsunam.features.domain.usecases.GetArtistSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetFavouriteSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetFeaturedArtistsUsecase
import com.example.geetsunam.features.domain.usecases.GetFeaturedSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetGenreSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetGenresUsecase
import com.example.geetsunam.features.domain.usecases.GetLocalTrendingUsecase
import com.example.geetsunam.features.domain.usecases.GetNewSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetRecommendedSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetSingleSongUsecase
import com.example.geetsunam.features.domain.usecases.GetTrendingSongsUsecase
import com.example.geetsunam.features.domain.usecases.GoogleLoginUsecase
import com.example.geetsunam.features.domain.usecases.LoginUsecase
import com.example.geetsunam.features.domain.usecases.SaveTrendingUsecase
import com.example.geetsunam.features.domain.usecases.SearchUsecase
import com.example.geetsunam.features.domain.usecases.SignupUsecase
import com.example.geetsunam.features.domain.usecases.ToggleFavouriteUsecase
import com.example.geetsunam.features.domain.usecases.TrackPlayedSongUsecase
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
    fun provideForgotPasswordUsecase(repo: UserRepository): ForgotPasswordUsecase {
        return ForgotPasswordUsecase(repo)
    }

    @Provides
    fun providesSignupUsecase(repo: UserRepository): SignupUsecase {
        return SignupUsecase(repo)
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

    @Provides
    fun provideSearchUsecase(repo: UserRepository): SearchUsecase {
        return SearchUsecase(repo)
    }

    @Provides
    @Singleton
    fun provideRecommendedSongsUsecase(repo: UserRepository): GetRecommendedSongsUsecase {
        return GetRecommendedSongsUsecase(repo)
    }

    @Provides
    fun provideTrackSongUsecase(repo: UserRepository): TrackPlayedSongUsecase {
        return TrackPlayedSongUsecase(repo)
    }

    @Provides
    fun provideChangePasswordUsecase(repo: UserRepository): ChangePasswordUsecase {
        return ChangePasswordUsecase(repo)
    }

    //USECASES FOR ROOM RELATED WORKS
    @Provides
    fun providesSaveTrendingUsecase(localDatasource: UserLocalDatasource): SaveTrendingUsecase {
        return SaveTrendingUsecase(localDatasource)
    }

    @Provides
    fun providesLocalTrendingUsecase(localDatasource: UserLocalDatasource): GetLocalTrendingUsecase {
        return GetLocalTrendingUsecase(localDatasource)
    }
}