package com.example.geetsunam.features.presentation.home.featured_artists.viewmodel

sealed class FeaturedArtistsEvent {
    data class GetFeaturedArtists(val token: String) : FeaturedArtistsEvent()
}