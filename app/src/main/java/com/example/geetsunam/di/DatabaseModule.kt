package com.example.geetsunam.di

import android.content.Context
import androidx.room.Room
import com.example.geetsunam.database.AppDao
import com.example.geetsunam.database.Appdatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesAppDatabase(context: Context): Appdatabase {
        return Room.databaseBuilder(context, Appdatabase::class.java, "geetsunam_db")
            .fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesAppDao(database: Appdatabase): AppDao {
        return database.appDao()
    }
}