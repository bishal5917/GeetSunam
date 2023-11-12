package com.example.geetsunam.features.presentation.music.viewmodel

import com.example.geetsunam.features.data.models.songs.SongResponseModel
import com.example.geetsunam.features.domain.entities.SongEntity

data class MusicState(
    val status: MusicStatus,
    val message: String? = null,
    val playlistName: String? = null,
    val currentSong: SongEntity? = null,
    val totalSongs: Int? = null,
    val currentPlaylist: SongResponseModel.Data? = null,
) {
    companion object {
        val idle = MusicState(MusicStatus.IDLE, message = "")
    }

    enum class MusicStatus {
        IDLE, PLAYING, PAUSED, RELEASED, RESET, EndOfPlaylist
    }
}