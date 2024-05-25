package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.database.entities.OfflineSong
import com.example.geetsunam.features.data.datasource.UserLocalDatasource

class SaveSongOfflineUsecase(private val localDatasource: UserLocalDatasource) {
    suspend fun call(song: OfflineSong) {
        localDatasource.saveOfflineSong(song)
    }
}