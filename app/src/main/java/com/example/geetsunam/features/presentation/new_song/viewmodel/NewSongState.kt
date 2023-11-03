package com.example.geetsunam.features.presentation.new_song.viewmodel

import com.example.geetsunam.features.data.models.songs.SongResponseModel

data class NewSongState(
    val status: NewSongStatus,
    val message: String? = null,
    val songs: SongResponseModel.Data? = null
) {
    companion object {
        val idle = NewSongState(NewSongStatus.IDLE, message = "")
    }

    enum class NewSongStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}