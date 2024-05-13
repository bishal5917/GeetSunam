package com.example.geetsunam.features.presentation.new_song.viewmodel

import com.example.geetsunam.utils.models.Song

data class NewSongState(
    val status: NewSongStatus,
    val message: String? = null,
    val fromApi: Boolean? = false,
    val songs: List<Song?>? = null
) {
    companion object {
        val idle = NewSongState(NewSongStatus.IDLE, message = "")
    }

    enum class NewSongStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}