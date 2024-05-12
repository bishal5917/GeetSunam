package com.example.geetsunam.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.geetsunam.database.entities.Trending

@Database(entities = [Trending::class], version = 1, exportSchema = false)
//@TypeConverters(Converters::class)
abstract class Appdatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}