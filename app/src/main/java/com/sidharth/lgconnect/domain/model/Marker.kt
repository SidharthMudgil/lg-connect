package com.sidharth.lgconnect.domain.model

import com.google.android.gms.maps.model.LatLng
import com.sidharth.lgconnect.R

data class Marker(
    val title: String,
    val subtitle: String,
    val latLng: LatLng,
) {
    val icon: Int = R.drawable.ic_marker
}