package com.sidharth.lgconnect.data.repository

import com.sidharth.lgconnect.data.local.LocalDataSource
import com.sidharth.lgconnect.domain.model.Chart
import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.domain.model.Planet
import com.sidharth.lgconnect.domain.model.Wonder
import com.sidharth.lgconnect.domain.repository.DataRepository
import com.sidharth.lgconnect.ui.observer.MarkersObserver
import com.sidharth.lgconnect.util.Constants

class DataRepositoryImpl(private val localDataSource: LocalDataSource) : DataRepository {
    private var markers = mutableListOf<Marker>()
    private val observers = mutableListOf<MarkersObserver>()

    override fun getAllPlanets(): List<Planet> {
        return Constants.planets
    }

    override fun getAllCharts(): List<Chart> {
        return Constants.charts
    }

    override fun getAllWonders(): List<Wonder> {
        return Constants.wonders
    }

    override suspend fun getAllMarkers(): MutableList<Marker> {
        return if (markers.isEmpty()) {
            localDataSource.getAllMarkers().apply {
                markers = this
            }
        } else {
            markers
        }
    }

    override suspend fun addMarker(marker: Marker) {
        markers.add(marker)
        localDataSource.addMarker(marker)
        notifyObservers()
    }

    override suspend fun deleteMarker(marker: Marker) {
        markers.remove(marker)
        localDataSource.deleteMarker(marker)
        notifyObservers()
    }

    override fun addObserver(observer: MarkersObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: MarkersObserver) {
        observers.remove(observer)
    }

    private fun notifyObservers() {
        observers.forEach { it.onDataChanged() }
    }
}
