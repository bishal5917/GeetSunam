package com.example.geetsunam.features.presentation.single_artist.viewmodel

import com.example.geetsunam.utils.models.CommonRequestModel

sealed class ArtistSongEvent {
    data class GetArtistSongs(val commonRequestModel: CommonRequestModel) : ArtistSongEvent()
}