package com.sidharth.lgconnect.ui.maps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sidharth.lgconnect.domain.usecase.AddMarkerUseCase
import com.sidharth.lgconnect.domain.usecase.AddObserverUseCase
import com.sidharth.lgconnect.domain.usecase.GetMarkersUseCase

class MapsViewModelFactory(
    private val getMarkersUseCase: GetMarkersUseCase,
    private val addMarkerUseCase: AddMarkerUseCase,
    private val addObserverUseCase: AddObserverUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsViewModel(
                addObserverUseCase,
                getMarkersUseCase,
                addMarkerUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}