package com.example.geetsunam.services.local

import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow

val USER_TOKEN = stringPreferencesKey("token")

interface LocalDatastore {

    fun getToken(): Flow<String>

    suspend fun saveToken(id: String?)

    suspend fun removeToken()
}