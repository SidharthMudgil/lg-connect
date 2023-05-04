package com.sidharth.lgconnect.data.local

import com.sidharth.lgconnect.domain.model.Marker

interface LocalDataSource {
    suspend fun getAllMarkers(): MutableList<Marker>
    suspend fun addMarker(marker: Marker)
    suspend fun deleteMarker(marker: Marker)
}