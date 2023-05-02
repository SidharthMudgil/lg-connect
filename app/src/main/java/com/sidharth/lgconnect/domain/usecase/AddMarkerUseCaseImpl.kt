package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.domain.repository.HomeRepository

class AddMarkerUseCaseImpl(private val repository: HomeRepository): ModifyMarkersUseCase {
    override suspend fun execute(marker: Marker) {
        repository.addMarker(marker)
    }
}