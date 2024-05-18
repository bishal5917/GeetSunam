package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.database.entities.FeaturedGenre
import com.example.geetsunam.features.data.datasource.UserLocalDatasource
import kotlinx.coroutines.flow.Flow

class GetLocalFeaturedGenresUsecase(private val localDatasource: UserLocalDatasource) {
    fun call(): Flow<List<FeaturedGenre>> {
        return localDatasource.getFeaturedGenres()
    }
}