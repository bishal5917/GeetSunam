package com.example.geetsunam.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.geetsunam.features.data.datasource.UserRemoteDatasource
import com.example.geetsunam.features.data.datasource.UserRemoteDatasourceImpl
import com.example.geetsunam.features.data.repositories.UserRepositoryImpl
import com.example.geetsunam.features.domain.repositories.UserRepository
import com.example.geetsunam.features.domain.usecases.GetFeaturedArtistsUsecase
import com.example.geetsunam.features.domain.usecases.GetFeaturedSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetGenresUsecase
import com.example.geetsunam.features.domain.usecases.LoginUsecase
import com.example.geetsunam.features.presentation.home.featured_artists.viewmodel.FeaturedArtistsViewModel
import com.example.geetsunam.features.presentation.home.featured_songs.viewmodel.FeaturedSongsViewModel
import com.example.geetsunam.features.presentation.home.genres.viewmodel.GenreViewModel
import com.example.geetsunam.features.presentation.login.viewmodel.LoginViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.services.local.LocalDatastore
import com.example.geetsunam.services.local.LocalDatastoreImpl
import com.example.geetsunam.services.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }),
            produceFile = { context.preferencesDataStoreFile("datastore") })
    }

    @Provides
    fun providesDataStore(datastore: DataStore<Preferences>): LocalDatastore =
        LocalDatastoreImpl(datastore)

    @Provides
    fun provideRemoteDataSource(apiService: ApiService): UserRemoteDatasource {
        return UserRemoteDatasourceImpl(apiService)
    }

    @Provides
    fun provideRepository(
        remoteDataSource: UserRemoteDatasource,
    ): UserRepository {
        return UserRepositoryImpl(remoteDataSource)
    }

    //registering usecases
    @Provides
    fun provideLoginUsecase(repo: UserRepository): LoginUsecase {
        return LoginUsecase(repo)
    }

    @Provides
    fun provideGenreUsecase(repo: UserRepository): GetGenresUsecase {
        return GetGenresUsecase(repo)
    }

    @Provides
    fun provideFeaturedArtistsUsecase(repo: UserRepository): GetFeaturedArtistsUsecase {
        return GetFeaturedArtistsUsecase(repo)
    }

    @Provides
    fun provideFeaturedSOngsUsecase(repo: UserRepository): GetFeaturedSongsUsecase {
        return GetFeaturedSongsUsecase(repo)
    }

    //registering viewmodels
    @Provides
    fun provideSplashViewModel(datastore: LocalDatastore): SplashViewModel {
        return SplashViewModel(datastore)
    }

    @Provides
    fun provideLoginViewModel(
        loginUsecase: LoginUsecase, localDatastore: LocalDatastore
    ): LoginViewModel {
        return LoginViewModel(loginUsecase, localDatastore)
    }

    @Provides
    fun provideGenreViewModel(
        getGenresUsecase: GetGenresUsecase
    ): GenreViewModel {
        return GenreViewModel(getGenresUsecase)
    }

    @Provides
    fun provideFeaturedArtistsViewModel(
        getFeaturedArtistsUsecase: GetFeaturedArtistsUsecase
    ): FeaturedArtistsViewModel {
        return FeaturedArtistsViewModel(getFeaturedArtistsUsecase)
    }

    @Provides
    fun provideFeaturedSomgsViewModel(
        getFeaturedSongsUsecase: GetFeaturedSongsUsecase
    ): FeaturedSongsViewModel {
        return FeaturedSongsViewModel(getFeaturedSongsUsecase)
    }
}