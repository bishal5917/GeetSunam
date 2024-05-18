package com.example.geetsunam.features.presentation.home.genres.viewmodel

import com.example.geetsunam.utils.models.Genre

sealed class GenreEvent {
    data class GetGenre(val token: String) : GenreEvent()
    data class RefreshGenres(val token: String) : GenreEvent()
    data class SaveGenres(val genres: List<Genre?>?) : GenreEvent()
}