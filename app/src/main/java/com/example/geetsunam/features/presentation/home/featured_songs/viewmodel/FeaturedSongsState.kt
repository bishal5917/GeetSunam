package com.example.geetsunam.features.presentation.home.featured_songs.viewmodel

import com.example.geetsunam.utils.models.Song

data class FeaturedSongsState(
    val status: SongStatus,
    val message: String? = null,
    val fromApi: Boolean? = false,
    val songs: List<Song?>? = null
) {
    companion object {
        val idle = FeaturedSongsState(SongStatus.IDLE, message = "")
    }

    enum class SongStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}