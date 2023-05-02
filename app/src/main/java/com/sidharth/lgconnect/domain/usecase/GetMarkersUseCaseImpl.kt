package com.sidharth.lgconnect.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.domain.repository.HomeRepository

class GetMarkersUseCaseImpl(private val repository: HomeRepository) : GetMarkersUseCase {
    override suspend fun execute(): MutableLiveData<MutableList<Marker>> {
        return repository.getAllMarkers()
    }
}