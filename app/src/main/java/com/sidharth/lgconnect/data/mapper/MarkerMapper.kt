package com.sidharth.lgconnect.data.mapper

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import com.sidharth.lgconnect.data.local.MarkerEntity
import com.sidharth.lgconnect.domain.model.Marker
import java.util.Locale

object MarkerMapper {

    @Suppress("DEPRECATION")
    fun mapAddressToMarker(context: Context, latitude: Double, longitude: Double): Marker? {
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

    fun fromEntity(entity: MarkerEntity): Marker {
        return Marker(
            title = entity.title,
            subtitle = entity.subtitle,
            latLng = LatLng(entity.latitude, entity.longitude)
        )
    }

    fun toEntity(marker: Marker): MarkerEntity {
        return MarkerEntity(
            title = marker.title,
            subtitle = marker.subtitle,
            latitude = marker.latLng.latitude,
            longitude = marker.latLng.longitude
        )
    }
}