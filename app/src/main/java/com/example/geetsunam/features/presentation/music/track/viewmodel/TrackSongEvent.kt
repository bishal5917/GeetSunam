package com.example.geetsunam.features.presentation.music.track.viewmodel

import com.example.geetsunam.utils.models.CommonRequestModel

sealed class TrackSongEvent {
    data class TrackCurrentSong(val commonRequestModel: CommonRequestModel) : TrackSongEvent()
}