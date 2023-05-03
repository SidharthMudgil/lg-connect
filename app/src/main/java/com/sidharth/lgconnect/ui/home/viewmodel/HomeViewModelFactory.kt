package com.sidharth.lgconnect.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sidharth.lgconnect.domain.usecase.AddObserverUseCase
import com.sidharth.lgconnect.domain.usecase.DeleteMarkerUseCase
import com.sidharth.lgconnect.domain.usecase.GetHomeDataUseCase
import com.sidharth.lgconnect.domain.usecase.GetMarkersUseCase

class HomeViewModelFactory(
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val getMarkersUseCase: GetMarkersUseCase,
    private val deleteMarkerUseCase: DeleteMarkerUseCase,
    private val addObserverUseCase: AddObserverUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
                getHomeDataUseCase,
                addObserverUseCase,
                getMarkersUseCase,
                deleteMarkerUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}