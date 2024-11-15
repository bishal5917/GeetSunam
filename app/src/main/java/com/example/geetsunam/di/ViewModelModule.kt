package com.example.geetsunam.di

import android.app.Application
import androidx.media3.exoplayer.ExoPlayer
import com.example.geetsunam.features.domain.usecases.ChangePasswordUsecase
import com.example.geetsunam.features.domain.usecases.DeleteFavouriteUsecase
import com.example.geetsunam.features.domain.usecases.DeleteOfflineSongUsecase
import com.example.geetsunam.features.domain.usecases.DeleteRecommendedUsecase
import com.example.geetsunam.features.domain.usecases.ForgotPasswordUsecase
import com.example.geetsunam.features.domain.usecases.GetArtistSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetFavouriteSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetFeaturedArtistsUsecase
import com.example.geetsunam.features.domain.usecases.GetFeaturedSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetGenreSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetGenresUsecase
import com.example.geetsunam.features.domain.usecases.GetLocalFavouriteUsecase
import com.example.geetsunam.features.domain.usecases.GetLocalFeaturedArtistsUsecase
import com.example.geetsunam.features.domain.usecases.GetLocalFeaturedGenresUsecase
import com.example.geetsunam.features.domain.usecases.GetLocalFeaturedSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetLocalNewUsecase
import com.example.geetsunam.features.domain.usecases.GetLocalRecommendedUsecase
import com.example.geetsunam.features.domain.usecases.GetLocalTrendingUsecase
import com.example.geetsunam.features.domain.usecases.GetNewSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetOfflineSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetRecommendedSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetTrendingSongsUsecase
import com.example.geetsunam.features.domain.usecases.GoogleLoginUsecase
import com.example.geetsunam.features.domain.usecases.LoginUsecase
import com.example.geetsunam.features.domain.usecases.SaveFavouriteUsecase
import com.example.geetsunam.features.domain.usecases.SaveFeaturedArtistsUsecase
import com.example.geetsunam.features.domain.usecases.SaveFeaturedGenreUsecase
import com.example.geetsunam.features.domain.usecases.SaveFeaturedSongsUsecase
import com.example.geetsunam.features.domain.usecases.SaveNewUsecase
import com.example.geetsunam.features.domain.usecases.SaveRecommendedUsecase
import com.example.geetsunam.features.domain.usecases.SaveSongOfflineUsecase
import com.example.geetsunam.features.domain.usecases.SaveTrendingUsecase
import com.example.geetsunam.features.domain.usecases.SearchUsecase
import com.example.geetsunam.features.domain.usecases.SignupUsecase
import com.example.geetsunam.features.domain.usecases.ToggleFavouriteUsecase
import com.example.geetsunam.features.domain.usecases.TrackPlayedSongUsecase
import com.example.geetsunam.features.presentation.for_you.viewmodel.RecommendViewModel
import com.example.geetsunam.features.presentation.home.change_password.viewmodel.ChangePasswordViewModel
import com.example.geetsunam.features.presentation.home.featured_artists.viewmodel.FeaturedArtistsViewModel
import com.example.geetsunam.features.presentation.home.featured_songs.viewmodel.FeaturedSongsViewModel
import com.example.geetsunam.features.presentation.home.genres.viewmodel.GenreViewModel
import com.example.geetsunam.features.presentation.home.search.viewmodel.SearchViewModel
import com.example.geetsunam.features.presentation.liked_song.viewmodel.FavSongViewModel
import com.example.geetsunam.features.presentation.login.forgot_password.viewmodel.ForgotPasswordViewModel
import com.example.geetsunam.features.presentation.login.google_login_viewmodel.GoogleLoginViewModel
import com.example.geetsunam.features.presentation.login.viewmodel.LoginViewModel
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavViewModel
import com.example.geetsunam.features.presentation.music.track.viewmodel.TrackSongViewModel
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.features.presentation.new_song.viewmodel.NewSongViewModel
import com.example.geetsunam.features.presentation.offline_song.viewmodel.OfflineSongViewModel
import com.example.geetsunam.features.presentation.signup.viewmodel.SignupViewModel
import com.example.geetsunam.features.presentation.single_artist.viewmodel.ArtistSongViewModel
import com.example.geetsunam.features.presentation.single_genre.viewmodel.GenreSongViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.features.presentation.trending.viewmodel.TrendingViewModel
import com.example.geetsunam.datastore.LocalDatastore
import com.example.geetsunam.features.domain.usecases.AddFavouriteLocalUsecase
import com.example.geetsunam.features.domain.usecases.DeleteFavouriteLocalUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {
    @Provides
    @Singleton
    fun provideSplashViewModel(datastore: LocalDatastore): SplashViewModel {
        return SplashViewModel(datastore)
    }

    @Provides
    fun provideLoginViewModel(
        loginUsecase: LoginUsecase,
        localDatastore: LocalDatastore,
        deleteFavouriteUsecase: DeleteFavouriteUsecase,
        deleteRecommendedUsecase: DeleteRecommendedUsecase
    ): LoginViewModel {
        return LoginViewModel(
            loginUsecase, localDatastore, deleteFavouriteUsecase, deleteRecommendedUsecase
        )
    }

    @Provides
    fun forgotPasswordViewModel(
        forgotPasswordUsecase: ForgotPasswordUsecase
    ): ForgotPasswordViewModel {
        return ForgotPasswordViewModel(forgotPasswordUsecase)
    }

    @Provides
    fun provideSignupViewModel(
        signupUsecase: SignupUsecase
    ): SignupViewModel {
        return SignupViewModel(signupUsecase)
    }

    @Provides
    fun provideGoogleLoginViewModel(
        googleLoginUsecase: GoogleLoginUsecase, localDatastore: LocalDatastore
    ): GoogleLoginViewModel {
        return GoogleLoginViewModel(googleLoginUsecase, localDatastore)
    }

    @Provides
    @Singleton
    fun provideGenreViewModel(
        getGenresUsecase: GetGenresUsecase,
        saveFeaturedGenreUsecase: SaveFeaturedGenreUsecase,
        getLocalFeaturedGenresUsecase: GetLocalFeaturedGenresUsecase
    ): GenreViewModel {
        return GenreViewModel(
            getGenresUsecase, saveFeaturedGenreUsecase, getLocalFeaturedGenresUsecase
        )
    }

    @Provides
    @Singleton
    fun provideFeaturedArtistsViewModel(
        getFeaturedArtistsUsecase: GetFeaturedArtistsUsecase,
        saveFeaturedArtistsUsecase: SaveFeaturedArtistsUsecase,
        getLocalFeaturedArtistsUsecase: GetLocalFeaturedArtistsUsecase
    ): FeaturedArtistsViewModel {
        return FeaturedArtistsViewModel(
            getFeaturedArtistsUsecase, saveFeaturedArtistsUsecase, getLocalFeaturedArtistsUsecase
        )
    }

    @Provides
    @Singleton
    fun provideFeaturedSongsViewModel(
        getFeaturedSongsUsecase: GetFeaturedSongsUsecase,
        saveFeaturedSongsUsecase: SaveFeaturedSongsUsecase,
        getLocalFeaturedSongsUsecase: GetLocalFeaturedSongsUsecase
    ): FeaturedSongsViewModel {
        return FeaturedSongsViewModel(
            getFeaturedSongsUsecase, saveFeaturedSongsUsecase, getLocalFeaturedSongsUsecase
        )
    }

    @Provides
    @Singleton
    fun provideTrendingSongsViewModel(
        getTrendingSongsUsecase: GetTrendingSongsUsecase,
        saveTrendingUsecase: SaveTrendingUsecase,
        getLocalTrendingUsecase: GetLocalTrendingUsecase
    ): TrendingViewModel {
        return TrendingViewModel(
            getTrendingSongsUsecase, saveTrendingUsecase, getLocalTrendingUsecase
        )
    }

    @Provides
    @Singleton
    fun providesMusicViewModel(
        exoPlayer: ExoPlayer,
        application: Application,
        saveSongOfflineUsecase: SaveSongOfflineUsecase
    ): MusicViewModel {
        return MusicViewModel(exoPlayer, application, saveSongOfflineUsecase)
    }

    @Provides
    @Singleton
    fun provideNewSongsViewModel(
        getNewSongsUsecase: GetNewSongsUsecase,
        saveNewUsecase: SaveNewUsecase,
        getLocalNewUsecase: GetLocalNewUsecase
    ): NewSongViewModel {
        return NewSongViewModel(getNewSongsUsecase, saveNewUsecase, getLocalNewUsecase)
    }

    @Provides
    fun provideToggleFavViewModel(
        toggleFavouriteUsecase: ToggleFavouriteUsecase,
        addFavouriteLocalUsecase: AddFavouriteLocalUsecase,
        deleteFavouriteLocalUsecase: DeleteFavouriteLocalUsecase
    ): ToggleFavViewModel {
        return ToggleFavViewModel(
            toggleFavouriteUsecase, addFavouriteLocalUsecase, deleteFavouriteLocalUsecase
        )
    }

    @Provides
    @Singleton
    fun provideFavouriteSongViewModel(
        getFavouriteSongsUsecase: GetFavouriteSongsUsecase,
        saveFavouriteUsecase: SaveFavouriteUsecase,
        getLocalFavouriteUsecase: GetLocalFavouriteUsecase
    ): FavSongViewModel {
        return FavSongViewModel(
            getFavouriteSongsUsecase, saveFavouriteUsecase, getLocalFavouriteUsecase
        )
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

    @Provides
    fun provideSearchViewModel(
        searchUsecase: SearchUsecase
    ): SearchViewModel {
        return SearchViewModel(searchUsecase)
    }

    @Provides
    @Singleton
    fun provideRecommendedSongsViewModel(
        getRecommendedSongsUsecase: GetRecommendedSongsUsecase,
        saveRecommendedUsecase: SaveRecommendedUsecase,
        getLocalRecommendedUsecase: GetLocalRecommendedUsecase
    ): RecommendViewModel {
        return RecommendViewModel(
            getRecommendedSongsUsecase, saveRecommendedUsecase, getLocalRecommendedUsecase
        )
    }

    @Provides
    fun provideTrackSongViewModel(
        trackPlayedSongUsecase: TrackPlayedSongUsecase
    ): TrackSongViewModel {
        return TrackSongViewModel(trackPlayedSongUsecase)
    }

    @Provides
    fun provideChangePasswordViewModel(
        changePasswordUsecase: ChangePasswordUsecase
    ): ChangePasswordViewModel {
        return ChangePasswordViewModel(changePasswordUsecase)
    }

    @Provides
    @Singleton
    fun providesOfflineSongViewModel(
        getOfflineSongsUsecase: GetOfflineSongsUsecase,
        deleteOfflineSongUsecase: DeleteOfflineSongUsecase
    ): OfflineSongViewModel {
        return OfflineSongViewModel(getOfflineSongsUsecase, deleteOfflineSongUsecase)
    }
}