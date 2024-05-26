package com.example.geetsunam.features.data.datasource

import com.example.geetsunam.database.AppDao
import com.example.geetsunam.database.entities.Favourite
import com.example.geetsunam.database.entities.Featured
import com.example.geetsunam.database.entities.FeaturedArtist
import com.example.geetsunam.database.entities.FeaturedGenre
import com.example.geetsunam.database.entities.New
import com.example.geetsunam.database.entities.OfflineSong
import com.example.geetsunam.database.entities.Recommended
import com.example.geetsunam.database.entities.Trending
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserLocalDatasource {
    suspend fun saveTrending(songs: List<Trending>)
    fun getTrending(): Flow<List<Trending>>
    suspend fun saveNew(songs: List<New>)
    fun getNew(): Flow<List<New>>
    suspend fun saveFavourites(songs: List<Favourite>)
    fun getFavourites(): Flow<List<Favourite>>
    suspend fun saveFeaturedSongs(songs: List<Featured>)
    fun getFeaturedSongs(): Flow<List<Featured>>
    suspend fun saveRecommended(songs: List<Recommended>)
    fun getRecommended(): Flow<List<Recommended>>

    //Deleting tables
    suspend fun deleteFavourites()
    suspend fun deleteRecommended()

    suspend fun saveFeaturedGenres(genres: List<FeaturedGenre>)
    fun getFeaturedGenres(): Flow<List<FeaturedGenre>>
    suspend fun saveFeaturedArtists(artists: List<FeaturedArtist>)
    fun getFeaturedArtists(): Flow<List<FeaturedArtist>>

    //save offline song
    suspend fun saveOfflineSong(song: OfflineSong)

    //getting offline song
    fun getOfflineSongs(): Flow<List<OfflineSong>>

    //getting single offline song
    suspend fun deleteOfflineSong(id: String)
}

class UserLocalDatasourceImpl @Inject constructor(private val appDao: AppDao) :
    UserLocalDatasource {
    override suspend fun saveTrending(songs: List<Trending>) {
        appDao.saveTrending(songs)
    }

    override fun getTrending(): Flow<List<Trending>> {
        return appDao.getTrending()
    }

    override suspend fun saveNew(songs: List<New>) {
        appDao.saveNewSongs(songs)
    }

    override fun getNew(): Flow<List<New>> {
        return appDao.getNewSongs()
    }

    override suspend fun saveFavourites(songs: List<Favourite>) {
        appDao.saveFavourites(songs)
    }

    override fun getFavourites(): Flow<List<Favourite>> {
        return appDao.getFavourites()
    }

    override suspend fun saveFeaturedSongs(songs: List<Featured>) {
        appDao.saveFeaturedSongs(songs)
    }

    override fun getFeaturedSongs(): Flow<List<Featured>> {
        return appDao.getFeaturedSongs()
    }

    override suspend fun saveRecommended(songs: List<Recommended>) {
        appDao.saveRecommended(songs)
    }

    override fun getRecommended(): Flow<List<Recommended>> {
        return appDao.getRecommended()
    }

    override suspend fun deleteFavourites() {
        appDao.deleteFavourites()
    }

    override suspend fun deleteRecommended() {
        appDao.deleteRecommended()
    }

    override suspend fun saveFeaturedGenres(genres: List<FeaturedGenre>) {
        appDao.saveGenres(genres)
    }

    override fun getFeaturedGenres(): Flow<List<FeaturedGenre>> {
        return appDao.getGenres()
    }

    override suspend fun saveFeaturedArtists(artists: List<FeaturedArtist>) {
        appDao.saveArtists(artists)
    }

    override fun getFeaturedArtists(): Flow<List<FeaturedArtist>> {
        return appDao.getArtists()
    }

    override suspend fun saveOfflineSong(song: OfflineSong) {
        appDao.saveOfflineSong(song)
    }

    override fun getOfflineSongs(): Flow<List<OfflineSong>> {
        return appDao.getOfflineSongs()
    }

    override suspend fun deleteOfflineSong(id: String) {
        appDao.deleteOfflineSong(id)
    }
}