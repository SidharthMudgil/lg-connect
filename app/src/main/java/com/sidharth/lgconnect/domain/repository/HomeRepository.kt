package com.sidharth.lgconnect.domain.repository

import androidx.lifecycle.MutableLiveData
import com.sidharth.lgconnect.domain.model.Chart
import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.domain.model.Planet
import com.sidharth.lgconnect.domain.model.Wonder

interface HomeRepository {
    fun getAllPlanets(): List<Planet>
    fun getAllCharts(): List<Chart>
    fun getAllWonders(): List<Wonder>
    fun getAllMarkers(): MutableLiveData<MutableList<Marker>>
    fun addMarker(marker: Marker)
    fun deleteMarker(marker: Marker)
}