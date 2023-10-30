package com.example.geetsunam.features.data.models.genres

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
    ) {
        data class Genre(
            @SerializedName("_id")
            val id: String?,
            @SerializedName("image")
            val image: String?,
            @SerializedName("name")
            val name: String?
        )
    }
}