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
import com.example.geetsunam.features.domain.usecases.LoginUsecase
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

    //registering viewmodels
    @Provides
    fun provideSplashViewModel(datastore: LocalDatastore): SplashViewModel {
        return SplashViewModel(datastore)
    }

    @Provides
    fun provideLoginViewModel(loginUsecase: LoginUsecase,localDatastore: LocalDatastore): LoginViewModel {
        return LoginViewModel(loginUsecase,localDatastore)
    }
}