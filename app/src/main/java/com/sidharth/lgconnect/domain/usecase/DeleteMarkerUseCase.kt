package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.domain.model.Marker

interface DeleteMarkerUseCase {
    suspend fun execute(marker: Marker)
}