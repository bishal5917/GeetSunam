package com.example.geetsunam.features.presentation.music.viewmodel

import com.example.geetsunam.utils.models.Song

sealed class MusicEvent {
    data class SetPlaylist(val playlist: List<Song?>?, val playlistName: String) : MusicEvent()

    data class SetAndRetainPlaylist(val playlist: List<Song?>?, val playlistName: String) :
        MusicEvent()

    data class SetCurrentSong(val songId: String) : MusicEvent()

    object PlayNextSong : MusicEvent()

    object PlayPreviousSong : MusicEvent()

    object Shuffle : MusicEvent()

    object Reset : MusicEvent()
}