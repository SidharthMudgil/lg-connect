package com.sidharth.lgconnect.util

import android.app.Dialog
import android.content.Context

object LGConnectionDialog {
    private var dialog: Dialog? = null

    fun show(context: Context, onRetry: () -> Unit) {
//        dialog = Dialog(context)
//        dialog?.setContentView(R.layout.dialog_ssh_connection)
//        dialog?.setCancelable(false)
//
//        val retryButton = dialog?.findViewById<Button>(R.id.retry_button)
//        retryButton?.setOnClickListener {
//            dialog?.dismiss()
//            onRetry.invoke()
//        }
//
//        // Show the dialog
//        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
        dialog = null
    }
}
