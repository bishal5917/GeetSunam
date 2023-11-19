package com.example.geetsunam.features.presentation.music.track.viewmodel

data class TrackSongState(
    val status: TrackSongStatus, val message: String? = null
) {
    companion object {
        val idle = TrackSongState(TrackSongStatus.IDLE, message = "")
    }

    enum class TrackSongStatus {
        IDLE, TRACKING, TRACKED, FAILED
    }
}