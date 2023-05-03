package com.sidharth.lgconnect.domain.repository

import com.sidharth.lgconnect.domain.model.Chart
import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.domain.model.Planet
import com.sidharth.lgconnect.domain.model.Wonder
import com.sidharth.lgconnect.ui.observers.MarkersObserver

interface DataRepository {
    fun getAllPlanets(): List<Planet>
    fun getAllCharts(): List<Chart>
    fun getAllWonders(): List<Wonder>
    suspend fun getAllMarkers(): MutableList<Marker>
    suspend fun addMarker(marker: Marker)
    suspend fun deleteMarker(marker: Marker)
    fun addObserver(observer: MarkersObserver)
    fun removeObserver(observer: MarkersObserver)
}