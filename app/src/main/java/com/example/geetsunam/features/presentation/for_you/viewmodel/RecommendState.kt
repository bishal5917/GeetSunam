package com.example.geetsunam.features.presentation.for_you.viewmodel

import com.example.geetsunam.utils.models.Song

data class RecommendState(
    val status: RecommendStatus,
    val message: String? = null,
    val fromApi: Boolean? = false,
    val songs: List<Song?>? = null
) {
    companion object {
        val idle = RecommendState(RecommendStatus.IDLE, message = "")
    }

    enum class RecommendStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}