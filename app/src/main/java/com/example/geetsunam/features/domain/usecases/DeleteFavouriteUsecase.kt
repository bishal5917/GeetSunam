package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.features.data.datasource.UserLocalDatasource

class DeleteFavouriteUsecase(private val localDatasource: UserLocalDatasource) {
    suspend fun call() {
        localDatasource.deleteFavourites()
    }
}