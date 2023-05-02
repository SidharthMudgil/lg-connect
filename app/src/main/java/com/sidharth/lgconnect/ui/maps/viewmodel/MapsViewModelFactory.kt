package com.sidharth.lgconnect.ui.maps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sidharth.lgconnect.domain.usecase.AddMarkerUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.GetMarkersUseCaseImpl
import com.sidharth.lgconnect.ui.home.viewmodel.HomeViewModel

class MapsViewModelFactory(
    private val getMarkersUseCaseImpl: GetMarkersUseCaseImpl,
    private val addMarkerUseCaseImpl: AddMarkerUseCaseImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsViewModel(
                getMarkersUseCaseImpl,
                addMarkerUseCaseImpl
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}