package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.features.data.datasource.UserLocalDatasource

class DeleteOfflineSongUsecase(private val localDatasource: UserLocalDatasource) {
    suspend fun call(songId: String) {
        localDatasource.deleteOfflineSong(songId)
    }
}