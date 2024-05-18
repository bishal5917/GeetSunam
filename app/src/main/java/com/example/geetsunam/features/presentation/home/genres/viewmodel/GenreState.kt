package com.example.geetsunam.features.presentation.home.genres.viewmodel

import com.example.geetsunam.utils.models.Genre

data class GenreState(
    val status: GenreStatus,
    val message: String? = null,
    val fromApi: Boolean? = false,
    val genres: List<Genre?>? = null
) {
    companion object {
        val idle = GenreState(GenreStatus.IDLE, message = "")
    }

    enum class GenreStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}