package com.example.geetsunam.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.geetsunam.database.entities.Favourite
import com.example.geetsunam.database.entities.Featured
import com.example.geetsunam.database.entities.New
import com.example.geetsunam.database.entities.Recommended
import com.example.geetsunam.database.entities.Trending

@Database(
    entities = [Trending::class, New::class, Favourite::class, Featured::class, Recommended::class],
    version = 3,
    exportSchema = false
)
//@TypeConverters(Converters::class)
abstract class Appdatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}