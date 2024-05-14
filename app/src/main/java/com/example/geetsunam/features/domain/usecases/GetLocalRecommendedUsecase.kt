package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.database.entities.Recommended
import com.example.geetsunam.features.data.datasource.UserLocalDatasource
import kotlinx.coroutines.flow.Flow

class GetLocalRecommendedUsecase(private val localDatasource: UserLocalDatasource) {
    fun call(): Flow<List<Recommended>> {
        return localDatasource.getRecommended()
    }
}