package com.example.geetsunam.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Trending")
data class Trending(
    @PrimaryKey val id: String,
    val coverArt: String?,
    val artistName: String?,
    val songName: String?,
    val duration: String?,
    val source: String?,
    val stream: String?,
    val isFavourite: Boolean?,
)