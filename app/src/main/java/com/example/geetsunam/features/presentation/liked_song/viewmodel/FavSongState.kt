package com.example.geetsunam.features.presentation.liked_song.viewmodel

import com.example.geetsunam.utils.models.Song

data class FavSongState(
    val status: FavSongStatus,
    val message: String? = null,
    val fromApi: Boolean? = false,
    val songs: List<Song?>? = null
) {
    companion object {
        val idle = FavSongState(FavSongStatus.IDLE, message = "")
    }

    enum class FavSongStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}