package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.database.entities.New
import com.example.geetsunam.features.data.datasource.UserLocalDatasource
import kotlinx.coroutines.flow.Flow

class GetLocalNewUsecase(private val localDatasource: UserLocalDatasource) {
    fun call(): Flow<List<New>> {
        return localDatasource.getNew()
    }
}