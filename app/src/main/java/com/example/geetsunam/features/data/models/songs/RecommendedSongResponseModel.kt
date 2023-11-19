package com.example.geetsunam.features.data.models.songs

import com.example.geetsunam.utils.models.Song
import com.google.gson.annotations.SerializedName

data class RecommendedSongResponseModel(
    @SerializedName("data") val `data`: List<Song?>?,
    @SerializedName("results") val results: Int?,
    @SerializedName("status") val status: String?
)