package com.example.geetsunam.features.data.models.artist

import com.google.gson.annotations.SerializedName

data class ArtistResponseModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("results")
    val results: Int?,
    @SerializedName("status")
    val status: String?
) {
    data class Data(
        @SerializedName("artists")
        val artists: List<Artist?>?
    ) {
        data class Artist(
            @SerializedName("fullname")
            val fullname: String?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("isFeatured")
            val isFeatured: Boolean?,
            @SerializedName("profileImage")
            val profileImage: String?
        )
    }
}