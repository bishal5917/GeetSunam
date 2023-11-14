package com.example.geetsunam.utils.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artist(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("fullname")
    val fullname: String? = null,
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("isFeatured")
    val isFeatured: Boolean? = null,
    @SerializedName("profileImage")
    val profileImage: String? = null
) : Parcelable