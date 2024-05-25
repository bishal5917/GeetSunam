package com.example.geetsunam.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.geetsunam.database.converters.ByteArrayConverter

@Entity(tableName = "OfflineSong")
@TypeConverters(ByteArrayConverter::class)
data class OfflineSong(
    @PrimaryKey val id: String,
    val coverArt: String?,
    val artistName: String?,
    val songName: String?,
    val duration: String?,
    val source: String?,
    val stream: String?,
    val isFavourite: Boolean?,
    val filePath: String,
    val fileData: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OfflineSong

        if (fileData != null) {
            if (other.fileData == null) return false
            if (!fileData.contentEquals(other.fileData)) return false
        } else if (other.fileData != null) return false

        return true
    }

    override fun hashCode(): Int {
        return fileData?.contentHashCode() ?: 0
    }
}