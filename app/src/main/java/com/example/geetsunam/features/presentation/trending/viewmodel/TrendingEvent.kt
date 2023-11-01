package com.example.geetsunam.features.presentation.trending.viewmodel

sealed class TrendingEvent {
    data class GetTrendingSongs(val token: String) : TrendingEvent()
}