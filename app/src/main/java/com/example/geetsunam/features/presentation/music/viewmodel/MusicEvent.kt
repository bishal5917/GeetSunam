package com.example.geetsunam.features.presentation.music.viewmodel

import com.example.geetsunam.features.data.models.songs.SongResponseModel

sealed class MusicEvent {
    data class SetPlaylist(val playlist: SongResponseModel.Data, val playlistName: String) :
        MusicEvent()

    data class SetCurrentSong(val songId: String) : MusicEvent()

    object PlayNextSong : MusicEvent()

    object PlayPreviousSong : MusicEvent()

    object Shuffle : MusicEvent()
}