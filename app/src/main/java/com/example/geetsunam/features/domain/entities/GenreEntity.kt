package com.example.geetsunam.features.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreEntity(
    val id: String?,
    val name: String?,
    val image: String?
) : Parcelable