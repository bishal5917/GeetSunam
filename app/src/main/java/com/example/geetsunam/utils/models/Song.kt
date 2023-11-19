package com.example.geetsunam.utils.models

import com.google.gson.annotations.SerializedName

data class Song(
    @SerializedName("artists") val artists: Artist? = null,
    @SerializedName("coverArt") val coverArt: String? = null,
    @SerializedName("duration") val duration: String? = null,
    @SerializedName("_id") val id: String? = null,
    @SerializedName("isFavourite") val isFavourite: Boolean? = null,
    @SerializedName("isFeatured") val isFeatured: Boolean? = null,
    @SerializedName("source") val source: String? = null,
    @SerializedName("stream") val stream: String? = null,
    @SerializedName("title") val title: String? = null,
)