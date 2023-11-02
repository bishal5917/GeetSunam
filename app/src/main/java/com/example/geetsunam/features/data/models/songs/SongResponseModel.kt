package com.example.geetsunam.features.data.models.songs

import com.example.geetsunam.utils.models.Song
import com.google.gson.annotations.SerializedName

data class SongResponseModel(
    @SerializedName("data") val `data`: Data?,
    @SerializedName("results") val results: Int?,
    @SerializedName("status") val status: String?
) {
    data class Data(
        @SerializedName("songs") val songs: List<Song?>?
    )
}