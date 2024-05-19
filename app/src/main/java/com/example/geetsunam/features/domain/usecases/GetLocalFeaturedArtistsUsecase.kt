package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.database.entities.FeaturedArtist
import com.example.geetsunam.features.data.datasource.UserLocalDatasource
import kotlinx.coroutines.flow.Flow

class GetLocalFeaturedArtistsUsecase(private val localDatasource: UserLocalDatasource) {
    fun call(): Flow<List<FeaturedArtist>> {
        return localDatasource.getFeaturedArtists()
    }
}