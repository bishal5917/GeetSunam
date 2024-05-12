package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.database.entities.Trending
import com.example.geetsunam.features.data.datasource.UserLocalDatasource
import com.example.geetsunam.utils.models.Song

class SaveTrendingUsecase(private val localDatasource: UserLocalDatasource) {
    suspend fun call(songs: List<Song?>?) {
        // Ensure songs is not null and not empty
        songs?.takeIf { it.isNotEmpty() }?.let { songList ->
            // Convert list of Song entities to Trending entities
            val trendings = songList.map { song ->
                Trending(
                    id = song?.id!!,
                    coverArt = song.coverArt,
                    artistName = song.artists?.fullname,
                    songName = song.title,
                    duration = song.duration,
                    source = song.source,
                    stream = song.stream,
                    isFavourite = song.isFavourite,
                )
            }
            // Save the list of Trending entities to room
            localDatasource.saveTrending(trendings)
        }
    }
}