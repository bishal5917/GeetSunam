package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.database.entities.FeaturedGenre
import com.example.geetsunam.features.data.datasource.UserLocalDatasource
import com.example.geetsunam.utils.models.Genre

class SaveFeaturedGenreUsecase(private val localDatasource: UserLocalDatasource) {
    suspend fun call(songs: List<Genre?>?) {
        // Ensure songs is not null and not empty
        songs?.takeIf { it.isNotEmpty() }?.let { songList ->
            val genres = songList.map { song ->
                FeaturedGenre(
                    id = song?.id!!, image = song.image, name = song.name
                )
            }
            localDatasource.saveFeaturedGenres(genres)
        }
    }
}