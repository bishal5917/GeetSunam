package com.example.geetsunam.features.presentation.single_genre.viewmodel

import com.example.geetsunam.utils.models.CommonRequestModel

sealed class GenreSongEvent {
    data class GetGenreSongs(val commonRequestModel: CommonRequestModel) : GenreSongEvent()
}