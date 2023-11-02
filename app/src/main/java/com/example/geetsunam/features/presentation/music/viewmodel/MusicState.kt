package com.example.geetsunam.features.presentation.music.viewmodel

import com.example.geetsunam.utils.models.Song

data class MusicState(
    val status: MusicStatus,
    val message: String? = null,
    val music: Song? = null
) {
    companion object {
        val idle = MusicState(MusicStatus.IDLE, message = "")
    }

    enum class MusicStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}