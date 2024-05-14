package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.database.entities.Featured
import com.example.geetsunam.features.data.datasource.UserLocalDatasource
import com.example.geetsunam.utils.models.Song

class SaveFeaturedSongsUsecase(private val localDatasource: UserLocalDatasource) {
    suspend fun call(songs: List<Song?>?) {
        // Ensure songs is not null and not empty
        songs?.takeIf { it.isNotEmpty() }?.let { songList ->
            val songs = songList.map { song ->
                Featured(
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
            localDatasource.saveFeaturedSongs(songs)
        }
    }
}