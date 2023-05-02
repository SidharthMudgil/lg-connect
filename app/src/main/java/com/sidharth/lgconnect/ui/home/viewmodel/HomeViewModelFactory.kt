package com.sidharth.lgconnect.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sidharth.lgconnect.domain.usecase.DeleteMarkerUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.GetHomeDataUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.GetMarkersUseCaseImpl

class HomeViewModelFactory(
    private val getHomeDataUseCaseImpl: GetHomeDataUseCaseImpl,
    private val getMarkersUseCaseImpl: GetMarkersUseCaseImpl,
    private val deleteMarkerUseCaseImpl: DeleteMarkerUseCaseImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
                getHomeDataUseCaseImpl,
                getMarkersUseCaseImpl,
                deleteMarkerUseCaseImpl
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}