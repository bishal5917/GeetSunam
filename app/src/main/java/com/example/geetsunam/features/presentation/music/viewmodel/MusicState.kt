package com.example.geetsunam.features.presentation.music.viewmodel

import androidx.media3.common.MediaItem
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
    val medias: List<MediaItem>? = null,
) {
    companion object {
        val idle = MusicState(MusicStatus.IDLE, PlayMode.Serial, message = "")
    }

    enum class MusicStatus {
        IDLE, Perparing, Playing, Paused, Changed, Downloading, Downloaded, Failed
    }

    enum class PlayMode {
        Serial, Random, LoopCurrent
    }
}