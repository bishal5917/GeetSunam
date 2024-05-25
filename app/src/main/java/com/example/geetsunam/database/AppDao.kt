package com.example.geetsunam.database

import androidx.room.*
import com.example.geetsunam.database.entities.Favourite
import com.example.geetsunam.database.entities.Featured
import com.example.geetsunam.database.entities.FeaturedArtist
import com.example.geetsunam.database.entities.FeaturedGenre
import com.example.geetsunam.database.entities.New
import com.example.geetsunam.database.entities.OfflineSong
import com.example.geetsunam.database.entities.Recommended
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavourites(favourites: List<Favourite>)

    @Query("SELECT * FROM Favourite")
    fun getFavourites(): Flow<List<Favourite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFeaturedSongs(featured: List<Featured>)

    @Query("SELECT * FROM Featured")
    fun getFeaturedSongs(): Flow<List<Featured>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecommended(recommended: List<Recommended>)

    @Query("SELECT * FROM Recommended")
    fun getRecommended(): Flow<List<Recommended>>

    @Query("DELETE FROM Favourite")
    suspend fun deleteFavourites()

    @Query("DELETE FROM Recommended")
    suspend fun deleteRecommended()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGenres(genres: List<FeaturedGenre>)

    @Query("SELECT * FROM FeaturedGenre")
    fun getGenres(): Flow<List<FeaturedGenre>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArtists(artists: List<FeaturedArtist>)

    @Query("SELECT * FROM FeaturedArtist")
    fun getArtists(): Flow<List<FeaturedArtist>>

    //Inserting offline song
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOfflineSong(song: OfflineSong)

    //getting offline songs
    @Query("SELECT * FROM OfflineSong")
    fun getOfflineSongs(): Flow<List<OfflineSong>>
}