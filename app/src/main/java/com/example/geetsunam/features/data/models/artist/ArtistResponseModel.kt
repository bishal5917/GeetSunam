package com.example.geetsunam.features.data.models.artist

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ArtistResponseModel(
    @SerializedName("data") val `data`: Data?,
    @SerializedName("results") val results: Int?,
    @SerializedName("status") val status: String?
) {
    data class Data(
        @SerializedName("artists") val artists: List<Artist?>?
    ) {
        @Parcelize
        data class Artist(
            @SerializedName("fullname") val fullname: String?,
            @SerializedName("_id") val id: String?,
            @SerializedName("isFeatured") val isFeatured: Boolean?,
            @SerializedName("profileImage") val profileImage: String?
        ) : Parcelable
    }
}