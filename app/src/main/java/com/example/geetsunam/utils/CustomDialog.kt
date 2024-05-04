package com.example.geetsunam.utils

import android.app.Activity
import android.app.AlertDialog
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

    fun showSureLogoutDialog(context: Activity, onClickedYes: () -> Unit) {
        val sureLogoutDialog =
            AlertDialog.Builder(context).setTitle(R.string.logout).setMessage(R.string.sure_logout)
                .setPositiveButton(R.string.yes) { _, _ ->
                    onClickedYes()
                }.setNegativeButton(R.string.no) { _, _ -> }
        sureLogoutDialog.show()
    }
}