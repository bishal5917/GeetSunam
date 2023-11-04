package com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel

data class ToggleFavState(
    val status: ToggleFavStatus, val message: String? = null, val isFavourite: Boolean? = null,
    val drawableId: Int? = null
) {
    companion object {
        val idle = ToggleFavState(ToggleFavStatus.IDLE, message = "")
    }

    enum class ToggleFavStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}