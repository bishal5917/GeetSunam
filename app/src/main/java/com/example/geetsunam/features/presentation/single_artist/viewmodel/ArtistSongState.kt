package com.example.geetsunam.features.presentation.single_artist.viewmodel

import com.example.geetsunam.features.data.models.songs.SongResponseModel

data class ArtistSongState(
    val status: ArtistSongStatus,
    val message: String? = null,
    val songs: SongResponseModel.Data? = null
) {
    companion object {
        val idle = ArtistSongState(ArtistSongStatus.IDLE, message = "")
    }

    enum class ArtistSongStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}