package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.features.data.datasource.UserLocalDatasource
import com.example.geetsunam.utils.models.Song

class DeleteFavouriteLocalUsecase(private val localDatasource: UserLocalDatasource) {
    suspend fun call(song: Song) {
        localDatasource.deleteFavourite(song.id ?: "0")
    }
}