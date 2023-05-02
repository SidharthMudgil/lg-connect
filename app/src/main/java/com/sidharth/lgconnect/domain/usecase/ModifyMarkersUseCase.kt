package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.domain.model.Marker

interface ModifyMarkersUseCase {
    suspend fun execute(marker: Marker)
}