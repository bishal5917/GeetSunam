package com.example.geetsunam.services.local

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class LocalDatastoreImpl(private val datastore: DataStore<Preferences>) : LocalDatastore {
    override fun getToken(): Flow<String> {
        return datastore.data.catch { emit(emptyPreferences()) }.map {
            it[USER_TOKEN] ?: ""
        }
    }

    override suspend fun saveToken(id: String?) {
        datastore.edit {
            it[USER_TOKEN] = id ?: ""
        }
    }

    override suspend fun removeToken() {
        datastore.edit {
            it[USER_TOKEN] = ""
        }
    }
}