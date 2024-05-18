package com.example.geetsunam.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class DateUtil {

    fun hasBeenOver5Days(loggedInTime: Long): Boolean {
        // Calculate the difference in milliseconds
        val difference = System.currentTimeMillis() - loggedInTime
        // Calculate the number of milliseconds in 5 days
        val fiveDaysInMillis = 1 * 60 * 60 * 1000L
//        val fiveDaysInMillis = 5 * 24 * 60 * 60 * 1000L
        // Check if the difference is greater than or equal to 5 days in milliseconds
        return difference >= fiveDaysInMillis
    }

    fun getCurrentDateTimeWithAMPM(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun formatDateTime(calendar: Calendar = Calendar.getInstance()): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun formatDuration(durationMillis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(durationMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}