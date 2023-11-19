package com.example.geetsunam.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
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
import com.example.geetsunam.services.local.LocalDatastore
import com.example.geetsunam.services.local.LocalDatastoreImpl
import com.example.geetsunam.services.network.ApiService
import com.google.gson.Gson
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
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun provideResources(context: Context): Resources {
        return context.resources
    }

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
        gson: Gson, resources: Resources
    ): UserRepository {
        return UserRepositoryImpl(remoteDataSource, gson, resources)
    }
}