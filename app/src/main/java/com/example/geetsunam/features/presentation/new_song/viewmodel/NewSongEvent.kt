package com.example.geetsunam.features.presentation.new_song.viewmodel

import com.example.geetsunam.utils.models.Song

sealed class NewSongEvent {
    data class GetNewSongs(val token: String) : NewSongEvent()
    data class RefreshNew(val token: String) : NewSongEvent()
    data class SaveNew(val songs: List<Song?>?) : NewSongEvent()
}