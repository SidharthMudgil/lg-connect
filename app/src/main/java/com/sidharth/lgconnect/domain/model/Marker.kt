package com.sidharth.lgconnect.domain.model

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.sidharth.lgconnect.R

data class Marker(
    val title: String,
    val subtitle: String,
    val latLng: LatLng,
    val color: Int
) {
    val icon: Int = R.drawable.ic_marker

    constructor(
        title: String,
        subtitle: String,
        latLng: LatLng,
        color: String
    ) : this(title, subtitle, latLng, Color.parseColor(color))
}