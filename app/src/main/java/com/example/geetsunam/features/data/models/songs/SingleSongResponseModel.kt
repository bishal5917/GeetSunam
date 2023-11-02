package com.example.geetsunam.features.data.models.songs


import com.example.geetsunam.utils.models.Song
import com.google.gson.annotations.SerializedName

data class SingleSongResponseModel(
    @SerializedName("data") val `data`: Data?, @SerializedName("status") val status: String?
) {
    data class Data(
        @SerializedName("songs") val song: Song?
    )
}