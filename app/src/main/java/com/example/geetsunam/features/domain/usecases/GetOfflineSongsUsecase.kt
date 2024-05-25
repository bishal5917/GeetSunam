package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.database.entities.OfflineSong
import com.example.geetsunam.features.data.datasource.UserLocalDatasource
import kotlinx.coroutines.flow.Flow

class GetOfflineSongsUsecase(private val localDatasource: UserLocalDatasource) {
    fun call(): Flow<List<OfflineSong>> {
        return localDatasource.getOfflineSongs()
    }
}