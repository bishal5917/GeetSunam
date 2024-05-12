package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.database.entities.Trending
import com.example.geetsunam.features.data.datasource.UserLocalDatasource
import kotlinx.coroutines.flow.Flow

class GetLocalTrendingUsecase(private val localDatasource: UserLocalDatasource) {
    fun call(): Flow<List<Trending>> {
        return localDatasource.getTrending()
    }
}