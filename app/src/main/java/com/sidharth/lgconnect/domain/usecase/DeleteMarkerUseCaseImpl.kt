package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.domain.repository.DataRepository

class DeleteMarkerUseCaseImpl(private val repository: DataRepository) : ModifyMarkersUseCase {
    override suspend fun execute(marker: Marker) {
        repository.deleteMarker(marker)
    }
}