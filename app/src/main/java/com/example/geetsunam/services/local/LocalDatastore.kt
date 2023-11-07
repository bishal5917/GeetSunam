package com.example.geetsunam.services.local

import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.geetsunam.features.domain.entities.UserEntity
import kotlinx.coroutines.flow.Flow

val USER_TOKEN = stringPreferencesKey("user")

interface LocalDatastore {

    fun getUser(): Flow<UserEntity?>

    suspend fun saveUser(userEntity: UserEntity?)

    suspend fun removeUser()
}