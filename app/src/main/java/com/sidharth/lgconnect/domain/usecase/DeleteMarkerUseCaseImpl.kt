package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.data.repository.HomeRepositoryImpl
import com.sidharth.lgconnect.domain.model.Marker

class DeleteMarkerUseCaseImpl(private val repository: HomeRepositoryImpl) : ModifyMarkersUseCase {
    override suspend fun execute(marker: Marker) {
        repository.deleteMarker(marker)
    }
}