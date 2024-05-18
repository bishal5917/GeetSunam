package com.example.geetsunam.features.data.models.genres

import com.example.geetsunam.utils.models.Genre
import com.google.gson.annotations.SerializedName

data class GenreResponseModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("results")
    val results: Int?,
    @SerializedName("status")
    val status: String?
) {
    data class Data(
        @SerializedName("genres")
        val genres: List<Genre?>?
    )
}