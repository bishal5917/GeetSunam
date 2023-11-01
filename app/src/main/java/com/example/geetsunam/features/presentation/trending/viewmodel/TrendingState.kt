package com.example.geetsunam.features.presentation.trending.viewmodel

import com.example.geetsunam.features.data.models.songs.SongResponseModel

data class TrendingState(
    val status: TrendingStatus,
    val message: String? = null,
    val songs: SongResponseModel.Data? = null
) {
    companion object {
        val idle = TrendingState(TrendingStatus.IDLE, message = "")
    }

    enum class TrendingStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}