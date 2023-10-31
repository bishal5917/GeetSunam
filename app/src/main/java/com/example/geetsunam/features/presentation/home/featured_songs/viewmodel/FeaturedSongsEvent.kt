package com.example.geetsunam.features.presentation.home.featured_songs.viewmodel

sealed class FeaturedSongsEvent {
    data class GetFeaturedSongs(val token: String) : FeaturedSongsEvent()
}