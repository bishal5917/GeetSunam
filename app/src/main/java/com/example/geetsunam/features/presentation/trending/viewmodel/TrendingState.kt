package com.example.geetsunam.features.presentation.trending.viewmodel

import com.example.geetsunam.utils.models.Song

data class TrendingState(
    val status: TrendingStatus,
    val message: String? = null,
    val fromApi: Boolean? = false,
    val songs: List<Song?>? = null
) {
    companion object {
        val idle = TrendingState(TrendingStatus.IDLE, message = "")
    }

    enum class TrendingStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}