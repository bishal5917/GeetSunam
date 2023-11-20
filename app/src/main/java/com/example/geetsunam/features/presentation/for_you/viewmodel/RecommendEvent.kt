package com.example.geetsunam.features.presentation.for_you.viewmodel

sealed class RecommendEvent {
    data class GetRecommendedSongs(val token: String) : RecommendEvent()
    object Reset : RecommendEvent()
}