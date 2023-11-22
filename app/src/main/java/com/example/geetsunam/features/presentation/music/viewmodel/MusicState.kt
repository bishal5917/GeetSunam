package com.example.geetsunam.features.presentation.music.viewmodel

import com.example.geetsunam.features.domain.entities.SongEntity
import com.example.geetsunam.utils.models.Song

data class MusicState(
    val status: MusicStatus,
    val playMode: PlayMode,
    val message: String? = null,
    val playlistName: String? = null,
    val currentSong: SongEntity? = null,
    val totalSongs: Int? = null,
    val currentPlaylist: List<Song?>? = null,
) {
    companion object {
        val idle = MusicState(MusicStatus.IDLE, PlayMode.Serial, message = "")
    }

    enum class MusicStatus {
        IDLE, PREPARING, PLAYING, PAUSED, StartTracking
    }

    enum class PlayMode {
        Serial, Random, LoopCurrent
    }
}