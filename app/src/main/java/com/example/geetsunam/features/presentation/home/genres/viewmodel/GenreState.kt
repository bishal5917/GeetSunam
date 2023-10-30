package com.example.geetsunam.features.presentation.home.genres.viewmodel

import com.example.geetsunam.features.data.models.genres.GenreResponseModel

data class GenreState(
    val status: GenreStatus,
    val message: String? = null,
    val genres: GenreResponseModel.Data? = null
) {
    companion object {
        val idle = GenreState(GenreStatus.IDLE, message = "")
    }

    enum class GenreStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}