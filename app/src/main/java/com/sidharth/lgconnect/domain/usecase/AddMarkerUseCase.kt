package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.domain.model.Marker

interface AddMarkerUseCase {
    suspend fun execute(marker: Marker)
}