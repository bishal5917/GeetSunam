package com.example.geetsunam.features.presentation.music.viewmodel

import com.example.geetsunam.features.data.models.songs.SongResponseModel

sealed class MusicEvent {
    data class PlayMusic(val music: SongResponseModel.Data.Song) : MusicEvent()
}