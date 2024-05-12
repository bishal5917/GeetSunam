package com.example.geetsunam.features.data.datasource

import com.example.geetsunam.database.AppDao
import com.example.geetsunam.database.entities.Trending
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserLocalDatasource {
    suspend fun saveTrending(trending: List<Trending>)
    suspend fun getTrending(): Flow<List<Trending>>
}

class UserLocalDatasourceImpl @Inject constructor(private val appDao: AppDao) :
    UserLocalDatasource {
    override suspend fun saveTrending(trending: List<Trending>) {
        appDao.saveTrending(trending)
    }

    override suspend fun getTrending(): Flow<List<Trending>> {
        return appDao.getTrending()
    }
}