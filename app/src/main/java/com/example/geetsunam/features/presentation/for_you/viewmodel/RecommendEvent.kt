package com.example.geetsunam.features.presentation.for_you.viewmodel

import com.example.geetsunam.utils.models.Song

sealed class RecommendEvent {
    data class GetRecommendedSongs(val token: String) : RecommendEvent()
    data class RefreshRecommended(val token: String) : RecommendEvent()
    data class SaveRecommended(val songs: List<Song?>?) : RecommendEvent()
    object Reset : RecommendEvent()
}