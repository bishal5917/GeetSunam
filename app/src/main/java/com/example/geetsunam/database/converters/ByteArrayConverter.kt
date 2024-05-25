package com.example.geetsunam.database.converters

import androidx.room.TypeConverter

class ByteArrayConverter {
    @TypeConverter
    fun fromByteArray(byteArray: ByteArray): String {
        return byteArray.joinToString(separator = ",") { it.toString() }
    }

    @TypeConverter
    fun toByteArray(data: String): ByteArray {
        return data.split(",").map { it.toByte() }.toByteArray()
    }
}
