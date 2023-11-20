package com.example.geetsunam.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager

class LocalController {
    companion object

    fun unfocusKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = activity.currentFocus
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}