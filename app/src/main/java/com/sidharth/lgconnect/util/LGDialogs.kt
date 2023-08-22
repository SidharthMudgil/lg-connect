package com.sidharth.lgconnect.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.sidharth.lgconnect.R

class LGDialogs {
    private var connectionFailedDialog: Dialog? = null
    private var noConnectionDialog: Dialog? = null

    private fun initDialog(context: Context, type: Int) {
        val resourceProvider = ResourceProvider(context)

        when (type) {
            0 -> {
                connectionFailedDialog = Dialog(context)
                connectionFailedDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                connectionFailedDialog?.setCancelable(false)
                connectionFailedDialog?.setContentView(R.layout.lg_alert_dialog)
                connectionFailedDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val iv = connectionFailedDialog?.findViewById<ImageView>(R.id.iv)
                val titleTV = connectionFailedDialog?.findViewById<TextView>(R.id.tv_title)
                val descriptionTV =
                    connectionFailedDialog?.findViewById<TextView>(R.id.tv_description)
                val labelTV = connectionFailedDialog?.findViewById<TextView>(R.id.tv_label)
                val mcv = connectionFailedDialog?.findViewById<MaterialCardView>(R.id.mcv)

                iv?.setImageDrawable(resourceProvider.getDrawable(R.drawable.cartoon2))
                titleTV?.text = resourceProvider.getString(R.string.connection_failed_title)
                descriptionTV?.text =
                    resourceProvider.getString(R.string.connection_failed_description)
                labelTV?.text = resourceProvider.getString(R.string.connection_failed_button_text)
                mcv?.setOnClickListener {
                    connectionFailedDialog?.dismiss()
                }
            }

            else -> {
                noConnectionDialog = Dialog(context)
                noConnectionDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                noConnectionDialog?.setCancelable(false)
                noConnectionDialog?.setContentView(R.layout.lg_alert_dialog)
                noConnectionDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val iv = noConnectionDialog?.findViewById<ImageView>(R.id.iv)
                val titleTV = noConnectionDialog?.findViewById<TextView>(R.id.tv_title)
                val descriptionTV =
                    noConnectionDialog?.findViewById<TextView>(R.id.tv_description)
                val labelTV = noConnectionDialog?.findViewById<TextView>(R.id.tv_label)
                val mcv = noConnectionDialog?.findViewById<MaterialCardView>(R.id.mcv)

                iv?.setImageDrawable(resourceProvider.getDrawable(R.drawable.cartoon1))
                titleTV?.text = resourceProvider.getString(R.string.no_connection_title)
                descriptionTV?.text = resourceProvider.getString(R.string.no_connection_description)
                labelTV?.text = resourceProvider.getString(R.string.no_connection_button_text)
                mcv?.setOnClickListener {
                    noConnectionDialog?.dismiss()
                }
            }
        }
    }

    fun showConnectionFailedDialog(context: Context) {
        if (connectionFailedDialog == null) {
            initDialog(context, 0)
        }
        connectionFailedDialog?.show()
    }

    fun dismissConnectionFailedDialog() {
        connectionFailedDialog?.dismiss()
    }

    fun showNoConnectionDialog(context: Context) {
        if (noConnectionDialog == null) {
            initDialog(context, 1)
        }
        noConnectionDialog?.show()
    }

    fun dismissNoConnectionDialog() {
        noConnectionDialog?.dismiss()
    }
}