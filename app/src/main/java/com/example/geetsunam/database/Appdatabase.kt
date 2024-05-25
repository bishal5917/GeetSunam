package com.example.geetsunam.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.geetsunam.database.converters.ByteArrayConverter
import com.example.geetsunam.database.entities.Favourite
import com.example.geetsunam.database.entities.Featured
import com.example.geetsunam.database.entities.FeaturedArtist
import com.example.geetsunam.database.entities.FeaturedGenre
import com.example.geetsunam.database.entities.New
import com.example.geetsunam.database.entities.OfflineSong
import com.example.geetsunam.database.entities.Recommended
import com.example.geetsunam.database.entities.Trending

@Database(
    entities = [Trending::class, New::class, Favourite::class, Featured::class, Recommended::class, FeaturedGenre::class, FeaturedArtist::class, OfflineSong::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(ByteArrayConverter::class)

abstract class Appdatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}