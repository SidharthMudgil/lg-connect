package com.sidharth.lgconnect.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable

class ResourceProvider(val context: Context) {

    fun getColor(id: Int): Int {
        return context.getColor(id)
    }

    fun getString(id: Int): String {
        return context.getString(id)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawable(id: Int): Drawable {
        return context.getDrawable(id)!!
    }
}