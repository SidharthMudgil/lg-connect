package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.data.repository.DataRepositoryImpl
import com.sidharth.lgconnect.domain.model.Marker

class DeleteMarkerUseCaseImpl(private val repository: DataRepositoryImpl) : ModifyMarkersUseCase {
    override suspend fun execute(marker: Marker) {
        repository.deleteMarker(marker)
    }
}