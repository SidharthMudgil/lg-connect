package com.sidharth.lgconnect.data.repository

import com.sidharth.lgconnect.domain.model.Chart
import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.domain.model.Planet
import com.sidharth.lgconnect.domain.model.Wonder
import com.sidharth.lgconnect.domain.repository.HomeRepository
import com.sidharth.lgconnect.util.Constants

class HomeRepositoryImpl: HomeRepository {
//    TODO("Define local data source to get all markers, add markers, delete marker")

    override fun getAllPlanets(): List<Planet> {
        return Constants.planets
    }

    override fun getAllCharts(): List<Chart> {
        return Constants.charts
    }

    override fun getAllWonders(): List<Wonder> {
        return Constants.wonders
    }

    override fun getAllMarkers(): MutableList<Marker> {
        TODO("Not yet implemented")
    }

    override fun addMarker(marker: Marker) {
        TODO("Not yet implemented")
    }

    override fun deleteMarker(marker: Marker) {
        TODO("Not yet implemented")
    }
}