package com.example.geetsunam.di

import com.example.geetsunam.features.domain.usecases.GetArtistSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetFavouriteSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetFeaturedArtistsUsecase
import com.example.geetsunam.features.domain.usecases.GetFeaturedSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetGenreSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetGenresUsecase
import com.example.geetsunam.features.domain.usecases.GetNewSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetTrendingSongsUsecase
import com.example.geetsunam.features.domain.usecases.GoogleLoginUsecase
import com.example.geetsunam.features.domain.usecases.LoginUsecase
import com.example.geetsunam.features.domain.usecases.ToggleFavouriteUsecase
import com.example.geetsunam.features.presentation.home.featured_artists.viewmodel.FeaturedArtistsViewModel
import com.example.geetsunam.features.presentation.home.featured_songs.viewmodel.FeaturedSongsViewModel
import com.example.geetsunam.features.presentation.home.genres.viewmodel.GenreViewModel
import com.example.geetsunam.features.presentation.liked_song.viewmodel.FavSongViewModel
import com.example.geetsunam.features.presentation.login.google_login_viewmodel.GoogleLoginViewModel
import com.example.geetsunam.features.presentation.login.viewmodel.LoginViewModel
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavViewModel
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.features.presentation.new_song.viewmodel.NewSongViewModel
import com.example.geetsunam.features.presentation.single_artist.viewmodel.ArtistSongViewModel
import com.example.geetsunam.features.presentation.single_genre.viewmodel.GenreSongViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.features.presentation.trending.viewmodel.TrendingViewModel
import com.example.geetsunam.services.local.LocalDatastore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {
    //registering viewmodels
    @Provides
    @Singleton
    fun provideSplashViewModel(datastore: LocalDatastore): SplashViewModel {
        return SplashViewModel(datastore)
    }

    @Provides
    @Singleton
    fun provideLoginViewModel(
        loginUsecase: LoginUsecase, localDatastore: LocalDatastore
    ): LoginViewModel {
        return LoginViewModel(loginUsecase, localDatastore)
    }

    @Provides
    @Singleton
    fun provideGoogleLoginViewModel(
        googleLoginUsecase: GoogleLoginUsecase, localDatastore: LocalDatastore
    ): GoogleLoginViewModel {
        return GoogleLoginViewModel(googleLoginUsecase, localDatastore)
    }

    @Provides
    @Singleton
    fun provideGenreViewModel(
        getGenresUsecase: GetGenresUsecase
    ): GenreViewModel {
        return GenreViewModel(getGenresUsecase)
    }

    @Provides
    @Singleton
    fun provideFeaturedArtistsViewModel(
        getFeaturedArtistsUsecase: GetFeaturedArtistsUsecase
    ): FeaturedArtistsViewModel {
        return FeaturedArtistsViewModel(getFeaturedArtistsUsecase)
    }

    @Provides
    @Singleton
    fun provideFeaturedSongsViewModel(
        getFeaturedSongsUsecase: GetFeaturedSongsUsecase
    ): FeaturedSongsViewModel {
        return FeaturedSongsViewModel(getFeaturedSongsUsecase)
    }

    @Provides
    @Singleton
    fun provideTrendingSongsViewModel(
        getTrendingSongsUsecase: GetTrendingSongsUsecase
    ): TrendingViewModel {
        return TrendingViewModel(getTrendingSongsUsecase)
    }

    @Provides
    @Singleton
    fun providesMusicViewModel(
    ): MusicViewModel {
        return MusicViewModel()
    }

    @Provides
    @Singleton
    fun provideNewSongsViewModel(
        getNewSongsUsecase: GetNewSongsUsecase
    ): NewSongViewModel {
        return NewSongViewModel(getNewSongsUsecase)
    }

    @Provides
    fun provideToggleFavViewModel(
        toggleFavouriteUsecase: ToggleFavouriteUsecase
    ): ToggleFavViewModel {
        return ToggleFavViewModel(toggleFavouriteUsecase)
    }

    @Provides
    @Singleton
    fun provideFavouriteSongViewModel(
        getFavouriteSongsUsecase: GetFavouriteSongsUsecase
    ): FavSongViewModel {
        return FavSongViewModel(getFavouriteSongsUsecase)
    }

    @Provides
    fun provideGenreSongViewModel(
        getGenreSongsUsecase: GetGenreSongsUsecase
    ): GenreSongViewModel {
        return GenreSongViewModel(getGenreSongsUsecase)
    }

    @Provides
    fun provideArtistSongViewModel(
        getArtistSongsUsecase: GetArtistSongsUsecase
    ): ArtistSongViewModel {
        return ArtistSongViewModel(getArtistSongsUsecase)
    }
}