package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.domain.repository.DataRepository

class GetMarkersUseCaseImpl(private val repository: DataRepository) : GetMarkersUseCase {
    override suspend fun execute(): MutableList<Marker> {
        return repository.getAllMarkers()
    }
}