package com.example.geetsunam.utils

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext

class CustomToast {
    companion object {
        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message ?: "Message", Toast.LENGTH_SHORT)
                .show()
        }
    }
}