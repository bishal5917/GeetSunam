package com.example.geetsunam.features.data.datasource

import com.example.geetsunam.database.AppDao
import com.example.geetsunam.database.entities.Favourite
import com.example.geetsunam.database.entities.New
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
}