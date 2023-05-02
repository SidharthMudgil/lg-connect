package com.sidharth.lgconnect.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.sidharth.lgconnect.domain.model.Marker

interface GetMarkersUseCase {
    suspend fun execute(): MutableLiveData<MutableList<Marker>>
}