package com.example.geetsunam.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FeaturedArtist")
data class FeaturedArtist(
    @PrimaryKey val id: String,
    val email: String?,
    val fullname: String?,
    val isFeatured: Boolean?,
    val profileImage: String?
)