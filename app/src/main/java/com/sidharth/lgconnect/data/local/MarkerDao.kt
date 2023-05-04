package com.sidharth.lgconnect.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MarkerDao {
    @Query("SELECT * FROM markers")
    fun getAllMarkers(): List<MarkerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarker(marker: MarkerEntity)

    @Delete
    suspend fun deleteMarker(marker: MarkerEntity)
}
