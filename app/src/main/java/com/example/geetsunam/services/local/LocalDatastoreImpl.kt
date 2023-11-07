package com.example.geetsunam.services.local

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.geetsunam.features.domain.entities.UserEntity
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class LocalDatastoreImpl(private val datastore: DataStore<Preferences>) : LocalDatastore {
    val gson = Gson()
    override fun getUser(): Flow<UserEntity?> {
        return datastore.data.catch { emit(emptyPreferences()) }.map { preferences ->
            val userJson = preferences[USER_TOKEN]
            gson.fromJson(userJson, UserEntity::class.java)
        }
    }


    override suspend fun saveUser(userEntity: UserEntity?) {
        val userJson = gson.toJson(userEntity)
        datastore.edit { preferences ->
            preferences[USER_TOKEN] = userJson
        }
    }


    override suspend fun removeUser() {
        datastore.edit {
            it[USER_TOKEN] = ""
        }
    }
}