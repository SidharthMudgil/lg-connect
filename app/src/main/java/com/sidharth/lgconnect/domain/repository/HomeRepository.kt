package com.sidharth.lgconnect.domain.repository

import com.sidharth.lgconnect.domain.model.Chart
import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.domain.model.Planet
import com.sidharth.lgconnect.domain.model.Wonder

interface HomeRepository {
    fun getAllPlanets(): List<Planet>
    fun getAllCharts(): List<Chart>
    fun getAllWonders(): List<Wonder>
    fun getAllMarkers(): MutableList<Marker>
    fun addMarker(marker: Marker)
    fun deleteMarker(marker: Marker)
}