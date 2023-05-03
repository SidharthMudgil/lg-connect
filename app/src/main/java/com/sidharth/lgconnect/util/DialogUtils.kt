package com.sidharth.lgconnect.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.sidharth.lgconnect.R

class DialogUtils(
    context: Context,
    image: Drawable,
    title: String,
    description: String,
    buttonLabel: String,
    showButton: Boolean = true,
    private val onDialogButtonClick: () -> Unit = {},
) {
    private val dialog: Dialog = Dialog(context)

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.lg_alert_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val iv = dialog.findViewById<ImageView>(R.id.iv)
        val titleTV = dialog.findViewById<TextView>(R.id.tv_title)
        val descriptionTV = dialog.findViewById<TextView>(R.id.tv_description)
        val labelTV = dialog.findViewById<TextView>(R.id.tv_label)
        val mcv = dialog.findViewById<MaterialCardView>(R.id.mcv)

        iv.setImageDrawable(image)
        titleTV.text = title
        descriptionTV.text = description
        labelTV.text = buttonLabel
        mcv.setOnClickListener {
            onDialogButtonClick()
        }
        if (!showButton) mcv.visibility = View.GONE
    }


    fun show() {
        Handler(Looper.getMainLooper()).post {
            dialog.show()
        }
    }

    fun dismiss() {
        Handler(Looper.getMainLooper()).post {
            dialog.dismiss()
        }
    }
}
