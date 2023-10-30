package com.example.geetsunam.utils

import android.app.Dialog
import com.example.geetsunam.R

class CustomDialog {
    fun showLoadingDialog(dialog: Dialog) {
        dialog.setContentView(R.layout.custom_loading_dialog)
        dialog.show()
        dialog.setCancelable(false)
    }

    fun hideLoadingDialog(dialog: Dialog) {
        dialog.dismiss()
    }
}