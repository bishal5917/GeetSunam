package com.example.geetsunam.features.data.models.search

import com.example.geetsunam.utils.models.Artist
import com.example.geetsunam.utils.models.Song
import com.google.gson.annotations.SerializedName

data class SearchResponseModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("status")
    val status: String?
) {
    data class Data(
        @SerializedName("search")
        val search: Search?
    ) {
        data class Search(
            @SerializedName("artists")
            val artists: List<Artist?>?,
            @SerializedName("songs")
            val songs: List<Song?>?
        )
    }
}