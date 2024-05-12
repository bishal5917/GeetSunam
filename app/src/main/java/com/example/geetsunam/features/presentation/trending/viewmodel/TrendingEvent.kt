package com.example.geetsunam.features.presentation.trending.viewmodel

import com.example.geetsunam.utils.models.Song

sealed class TrendingEvent {
    data class GetTrendingSongs(val token: String) : TrendingEvent()
    data class SaveTrending(val songs: List<Song?>?) : TrendingEvent()
}