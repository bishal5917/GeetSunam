package com.example.geetsunam.features.presentation.liked_song.viewmodel

import com.example.geetsunam.features.data.models.songs.SongResponseModel

data class FavSongState(
    val status: FavSongStatus,
    val message: String? = null,
    val songs: SongResponseModel.Data? = null
) {
    companion object {
        val idle = FavSongState(FavSongStatus.IDLE, message = "")
    }

    enum class FavSongStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}