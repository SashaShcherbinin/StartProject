package com.start.presentation.common

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.start.R

open class UiHelper
constructor(private val context: Activity) {
    private var errorToast: Toast? = null
    private var messageToast: Toast? = null
    private var uploadingDialog: AlertDialog? = null
    private var messageDialog: AlertDialog? = null

    open fun showUploading(show: Boolean) {
        if (show) showUploading() else hideUploading()
    }

    open fun showErrorToast(message: String?) {
        if (errorToast != null) {
            errorToast!!.cancel()
        }
        errorToast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        errorToast!!.show()
    }

    open fun showMessage(message: String) {
        if (messageToast != null) {
            messageToast!!.cancel()
        }
        messageToast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        messageToast!!.show()
    }

    open fun showMessageDialog(title: String?, message: String?, buttonText: String? = null) {
        if (messageDialog != null) {
            messageDialog?.dismiss()
        }
        val builder = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
        builder.setPositiveButton(
            buttonText ?: context.getString(R.string.general_button_ok)
        ) { d, _ ->
            d.dismiss()
        }
        messageDialog = builder.create()
        messageDialog?.show()
    }

    private fun showUploading() {
        if (uploadingDialog == null) {
            uploadingDialog = MaterialAlertDialogBuilder(
                context,
            ).show()
            uploadingDialog!!.setCancelable(false)
        } else {
            uploadingDialog!!.show()
        }
    }

    private fun hideUploading() {
        if (uploadingDialog != null) {
            uploadingDialog!!.dismiss()
        }
    }
}
