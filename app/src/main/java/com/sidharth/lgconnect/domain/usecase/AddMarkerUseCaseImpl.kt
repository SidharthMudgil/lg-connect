package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.domain.repository.DataRepository

class AddMarkerUseCaseImpl(private val repository: DataRepository): AddMarkerUseCase {
    override suspend fun execute(marker: Marker) {
        repository.addMarker(marker)
    }
}