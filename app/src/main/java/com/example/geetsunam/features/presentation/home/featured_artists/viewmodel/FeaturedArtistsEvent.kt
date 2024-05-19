package com.example.geetsunam.features.presentation.home.featured_artists.viewmodel

import com.example.geetsunam.utils.models.Artist

sealed class FeaturedArtistsEvent {
    data class GetFeaturedArtists(val token: String) : FeaturedArtistsEvent()
    data class RefershArtists(val token: String) : FeaturedArtistsEvent()
    data class SaveArtists(val artists: List<Artist?>?) : FeaturedArtistsEvent()
}