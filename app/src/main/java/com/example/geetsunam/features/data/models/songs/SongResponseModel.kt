package com.example.geetsunam.features.data.models.songs

import com.google.gson.annotations.SerializedName

data class SongResponseModel(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("results")
    val results: Int?,
    @SerializedName("status")
    val status: String?
) {
    data class Data(
        @SerializedName("songs")
        val songs: List<Song?>?
    ) {
        data class Song(
            @SerializedName("artists")
            val artists: Artists?,
            @SerializedName("coverArt")
            val coverArt: String?,
            @SerializedName("duration")
            val duration: String?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("isFavourite")
            val isFavourite: Boolean?,
            @SerializedName("isFeatured")
            val isFeatured: Boolean?,
            @SerializedName("playCount")
            val playCount: String?,
            @SerializedName("public")
            val `public`: Boolean?,
            @SerializedName("releasedDate")
            val releasedDate: String?,
            @SerializedName("source")
            val source: String?,
            @SerializedName("stream")
            val stream: String?,
            @SerializedName("title")
            val title: String?,
            @SerializedName("uploadedDate")
            val uploadedDate: String?
        ) {
            data class Artists(
                @SerializedName("email")
                val email: String?,
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
}