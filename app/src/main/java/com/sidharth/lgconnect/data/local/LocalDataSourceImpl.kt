package com.sidharth.lgconnect.data.local

import com.sidharth.lgconnect.data.mapper.MarkerMapper
import com.sidharth.lgconnect.domain.model.Marker

class LocalDataSourceImpl(
    private val markerDao: MarkerDao
) : LocalDataSource {
    override suspend fun getAllMarkers(): MutableList<Marker> {
        return markerDao.getAllMarkers().map {
            MarkerMapper.fromEntity(it)
        }.toMutableList()
    }

    override suspend fun addMarker(marker: Marker) {
        markerDao.insertMarker(MarkerMapper.toEntity(marker))
    }

    override suspend fun deleteMarker(marker: Marker) {
        markerDao.deleteMarker(MarkerMapper.toEntity(marker))
    }
}