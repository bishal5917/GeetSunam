package com.example.geetsunam.features.presentation.home.featured_artists.viewmodel

import com.example.geetsunam.utils.models.Artist

data class FeaturedArtistsState(
    val status: FeaturedArtistsStatus,
    val message: String? = null,
    val fromApi: Boolean? = false,
    val artists: List<Artist?>? = null
) {
    companion object {
        val idle = FeaturedArtistsState(FeaturedArtistsStatus.IDLE, message = "")
    }

    enum class FeaturedArtistsStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}