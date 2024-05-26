package com.example.geetsunam.features.presentation.offline_song.viewmodel

sealed class OfflineSongEvent {
    object GetOfflineSongs : OfflineSongEvent()
    data class DeleteOfflineSong(
        val id: String, val filePath: String
    ) : OfflineSongEvent()
}