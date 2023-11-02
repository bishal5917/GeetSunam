package com.example.geetsunam.features.presentation.music.viewmodel


sealed class MusicEvent {
    data class GetMusic(val token: String, val songId: String) : MusicEvent()
}