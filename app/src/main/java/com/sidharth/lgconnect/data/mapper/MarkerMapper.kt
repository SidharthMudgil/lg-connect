package com.sidharth.lgconnect.data.mapper

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import com.sidharth.lgconnect.domain.model.Marker
import java.util.Locale

class MarkerMapper(private val context: Context) {

    @Suppress("DEPRECATION")
    fun mapAddressToMarker(latitude: Double, longitude: Double): Marker? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addressList = geocoder.getFromLocation(latitude, longitude, 1)

        return when {
            addressList == null -> null
            addressList.isEmpty() -> null
            else -> {
                val address = addressList[0]

                Marker(
                    title = address.getAddressLine(0).split(',').take(2).joinToString(", "),
                    subtitle = address.getAddressLine(0),
                    latLng = LatLng(latitude, longitude),
                )
            }
        }
    }
}