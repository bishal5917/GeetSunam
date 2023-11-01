package com.example.geetsunam.features.presentation.music.viewmodel

import com.example.geetsunam.features.data.models.songs.SongResponseModel

data class MusicState(
    val status: MusicStatus,
    val message: String? = null,
    val music: SongResponseModel.Data.Song? = null
) {
    companion object {
        val idle = MusicState(MusicStatus.IDLE, message = "")
    }

    enum class MusicStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}