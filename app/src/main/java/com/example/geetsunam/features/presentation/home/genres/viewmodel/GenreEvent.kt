package com.example.geetsunam.features.presentation.home.genres.viewmodel

sealed class GenreEvent {
    data class GetGenre(val token: String) : GenreEvent()
}