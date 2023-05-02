package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.data.repository.DataRepositoryImpl
import com.sidharth.lgconnect.domain.model.Marker

class GetMarkersUseCaseImpl(private val repository: DataRepositoryImpl) : GetMarkersUseCase {
    override suspend fun execute(): MutableList<Marker> {
        return repository.getAllMarkers()
    }
}