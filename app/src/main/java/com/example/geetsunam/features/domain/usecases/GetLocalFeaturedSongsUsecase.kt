package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.database.entities.Featured
import com.example.geetsunam.features.data.datasource.UserLocalDatasource
import kotlinx.coroutines.flow.Flow

class GetLocalFeaturedSongsUsecase(private val localDatasource: UserLocalDatasource) {
    fun call(): Flow<List<Featured>> {
        return localDatasource.getFeaturedSongs()
    }
}