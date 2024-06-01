package com.example.geetsunam.features.domain.usecases

import com.example.geetsunam.database.entities.Favourite
import com.example.geetsunam.features.data.datasource.UserLocalDatasource
import com.example.geetsunam.utils.models.Song

class AddFavouriteLocalUsecase(private val localDatasource: UserLocalDatasource) {
    suspend fun call(song: Song?) {
        val fav = Favourite(
            id = song?.id!!,
            coverArt = song.coverArt,
            artistName = song.artists?.fullname,
            songName = song.title,
            duration = song.duration,
            source = song.source,
            stream = song.stream,
            isFavourite = song.isFavourite,
        )
        localDatasource.addFavourite(fav)
    }
}