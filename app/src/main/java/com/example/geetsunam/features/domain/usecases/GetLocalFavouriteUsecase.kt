package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.database.entities.Favourite
import com.example.geetsunam.features.data.datasource.UserLocalDatasource
import kotlinx.coroutines.flow.Flow

class GetLocalFavouriteUsecase(private val localDatasource: UserLocalDatasource) {
    fun call(): Flow<List<Favourite>> {
        return localDatasource.getFavourites()
    }
}