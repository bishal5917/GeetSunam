package com.example.geetsunam.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FeaturedGenre")
data class FeaturedGenre(
    @PrimaryKey val id: String, val image: String?, val name: String?
)