package com.example.geetsunam.features.presentation.home.featured_artists.viewmodel

import com.example.geetsunam.features.data.models.artist.ArtistResponseModel

data class FeaturedArtistsState(
    val status: FeaturedArtistsStatus,
    val message: String? = null,
    val artists: ArtistResponseModel.Data? = null
) {
    companion object {
        val idle = FeaturedArtistsState(FeaturedArtistsStatus.IDLE, message = "")
    }

    enum class FeaturedArtistsStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}