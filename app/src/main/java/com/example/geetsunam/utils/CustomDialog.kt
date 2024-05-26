package com.example.geetsunam.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
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

    fun showDownloadingDialog(dialog: Dialog) {
        dialog.setContentView(R.layout.downloading_dialog)
        dialog.show()
        dialog.setCancelable(false)
    }

    fun hideDownloadingDialog(dialog: Dialog) {
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

    fun showSureDownloadDialog(context: Activity, onClickedYes: () -> Unit) {
        val sureDownloadDialog = AlertDialog.Builder(context).setTitle(R.string.download)
            .setMessage(R.string.sure_download).setPositiveButton(R.string.yes) { _, _ ->
                onClickedYes()
            }.setNegativeButton(R.string.no) { _, _ -> }
        sureDownloadDialog.show()
    }

    fun showSureDeleteDialog(context: Context, onClickedYes: () -> Unit) {
        val sureDeleteDialog = AlertDialog.Builder(context).setTitle(R.string.delete)
            .setMessage(R.string.sure_delete).setPositiveButton(R.string.yes) { _, _ ->
                onClickedYes()
            }.setNegativeButton(R.string.no) { _, _ -> }
        sureDeleteDialog.show()
    }
}