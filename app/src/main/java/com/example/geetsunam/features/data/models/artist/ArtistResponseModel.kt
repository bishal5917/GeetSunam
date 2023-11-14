package com.example.geetsunam.features.data.models.artist

import com.example.geetsunam.utils.models.Artist
import com.google.gson.annotations.SerializedName

data class ArtistResponseModel(
    @SerializedName("data") val `data`: Data?,
    @SerializedName("results") val results: Int?,
    @SerializedName("status") val status: String?
) {
    data class Data(
        @SerializedName("artists") val artists: List<Artist?>?
    )
}