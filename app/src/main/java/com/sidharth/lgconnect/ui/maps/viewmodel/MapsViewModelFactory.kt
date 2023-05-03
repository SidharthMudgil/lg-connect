package com.sidharth.lgconnect.ui.maps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sidharth.lgconnect.domain.usecase.AddObserverUseCase
import com.sidharth.lgconnect.domain.usecase.GetMarkersUseCase
import com.sidharth.lgconnect.domain.usecase.ModifyMarkersUseCase

class MapsViewModelFactory(
    private val getMarkersUseCase: GetMarkersUseCase,
    private val addMarkerUseCase: ModifyMarkersUseCase,
    private val addObserverUseCase: AddObserverUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsViewModel(
                getMarkersUseCase,
                addMarkerUseCase,
                addObserverUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}