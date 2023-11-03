package com.example.geetsunam.features.presentation.new_song.viewmodel

sealed class NewSongEvent {
    data class GetNewSongs(val token: String) : NewSongEvent()
}