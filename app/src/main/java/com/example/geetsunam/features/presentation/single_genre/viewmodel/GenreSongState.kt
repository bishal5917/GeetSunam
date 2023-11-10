package com.example.geetsunam.features.presentation.single_genre.viewmodel

import com.example.geetsunam.features.data.models.songs.SongResponseModel

data class GenreSongState(
    val status: GenreSongStatus,
    val message: String? = null,
    val songs: SongResponseModel.Data? = null
) {
    companion object {
        val idle = GenreSongState(GenreSongStatus.IDLE, message = "")
    }

    enum class GenreSongStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}