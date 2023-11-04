package com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel

import com.example.geetsunam.utils.models.CommonRequestModel

sealed class ToggleFavEvent {
    data class AddFavourite(val commonRequestModel: CommonRequestModel) : ToggleFavEvent()
}