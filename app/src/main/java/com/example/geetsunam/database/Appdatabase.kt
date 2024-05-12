package com.example.geetsunam.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.geetsunam.database.entities.New
import com.example.geetsunam.database.entities.Trending

@Database(entities = [Trending::class, New::class], version = 2, exportSchema = false)
//@TypeConverters(Converters::class)
abstract class Appdatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}