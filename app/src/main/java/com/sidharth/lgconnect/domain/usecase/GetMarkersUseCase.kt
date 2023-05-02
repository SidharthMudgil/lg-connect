package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.domain.model.Marker

interface GetMarkersUseCase {
    suspend fun execute(): MutableList<Marker>
}