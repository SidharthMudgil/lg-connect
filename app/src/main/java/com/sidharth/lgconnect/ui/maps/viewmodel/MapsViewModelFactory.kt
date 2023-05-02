package com.sidharth.lgconnect.ui.maps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sidharth.lgconnect.domain.usecase.AddMarkerUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.AddObserverUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.GetMarkersUseCaseImpl

class MapsViewModelFactory(
    private val getMarkersUseCaseImpl: GetMarkersUseCaseImpl,
    private val addMarkerUseCaseImpl: AddMarkerUseCaseImpl,
    private val addObserverUseCaseImpl: AddObserverUseCaseImpl,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsViewModel(
                getMarkersUseCaseImpl,
                addMarkerUseCaseImpl,
                addObserverUseCaseImpl
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}