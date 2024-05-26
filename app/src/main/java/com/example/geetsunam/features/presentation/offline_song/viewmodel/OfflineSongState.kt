package com.example.geetsunam.features.presentation.offline_song.viewmodel

import com.example.geetsunam.utils.models.Song

data class OfflineSongState(
    val status: OfflineSongStatus, val message: String? = null, val songs: List<Song?>? = null
) {
    companion object {
        val idle = OfflineSongState(OfflineSongStatus.IDLE, message = "")
    }

    enum class OfflineSongStatus {
        IDLE, LOADING, SUCCESS, EMPTY, FAILED, DELETING, DELETED
    }
}