package com.example.geetsunam.database

import androidx.room.*
import com.example.geetsunam.database.entities.New
import com.example.geetsunam.database.entities.Trending
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTrending(trending: List<Trending>)

    @Query("SELECT * FROM Trending")
    fun getTrending(): Flow<List<Trending>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNewSongs(trending: List<New>)

    @Query("SELECT * FROM New")
    fun getNewSongs(): Flow<List<New>>
}