package com.example.geetsunam.utils

import java.io.File
import java.io.FileInputStream
import java.io.IOException

class FileUtil {

    public fun deleteFile(filePath: String): Boolean {
        val file = File(filePath)
        return if (file.exists()) {
            val deleted = file.delete()
            if (deleted) {
                LogUtil.log("File Deleted")
            } else {
                LogUtil.log("Deletion failed")
            }
            deleted
        } else {
            LogUtil.log("File not found")
            false
        }
    }

    public fun getFileAsByteArray(filePath: String): ByteArray? {
        val file = File(filePath)
        var fileInputStream: FileInputStream? = null
        return try {
            fileInputStream = FileInputStream(file)
            val bytes = ByteArray(file.length().toInt())
            fileInputStream.read(bytes)
            bytes
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            fileInputStream?.close()
        }
    }
}