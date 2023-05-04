package com.sidharth.lgconnect.data.local

import androidx.room.Entity

@Entity(tableName = "markers", primaryKeys = ["latitude", "longitude"])
data class MarkerEntity(
    val title: String,
    val subtitle: String,
    val latitude: Double,
    val longitude: Double
)
