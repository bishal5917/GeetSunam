package com.example.geetsunam.features.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongEntity(
    val id: String?,
    val coverArt: String?,
    val artistName: String?,
    val songName: String?,
    val duration: String?,
    val source: String?,
    val stream: String?,
    val isFavourite: Boolean?,
) : Parcelable