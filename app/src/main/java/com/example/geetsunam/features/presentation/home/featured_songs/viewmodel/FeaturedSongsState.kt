package com.example.geetsunam.features.presentation.home.featured_songs.viewmodel

import com.example.geetsunam.features.data.models.songs.SongResponseModel

data class FeaturedSongsState(
    val status: SongStatus, val message: String? = null, val songs: SongResponseModel.Data? = null
) {
    companion object {
        val idle = FeaturedSongsState(SongStatus.IDLE, message = "")
    }

    enum class SongStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}