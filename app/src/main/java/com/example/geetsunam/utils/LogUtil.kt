package com.example.geetsunam.utils

import android.util.Log

object LogUtil {
    const val Normal = "gs"
    const val SPLASH = "splash"
    const val GOOGLE = "google"
    const val PLAYER = "player"

    fun log(message: String?) {
        Log.d(Normal, message ?: "Logging to console!!!")
    }
}