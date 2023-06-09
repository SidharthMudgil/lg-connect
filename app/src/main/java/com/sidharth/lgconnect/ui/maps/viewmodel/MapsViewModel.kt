package com.sidharth.lgconnect.ui.maps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.domain.usecase.AddMarkerUseCase
import com.sidharth.lgconnect.domain.usecase.AddObserverUseCase
import com.sidharth.lgconnect.domain.usecase.GetMarkersUseCase
import com.sidharth.lgconnect.ui.observer.MarkersObserver
import kotlinx.coroutines.launch

class MapsViewModel(
    addObserverUseCase: AddObserverUseCase,
    private val getMarkersUseCase: GetMarkersUseCase,
    private val addMarkerUseCase: AddMarkerUseCase,
) : ViewModel(), MarkersObserver {

    private val _markers = MutableLiveData<MutableList<Marker>>()
    val markers: LiveData<MutableList<Marker>>
        get() = _markers

    init {
        viewModelScope.launch {
            _markers.postValue(getMarkersUseCase.execute())
        }
        addObserverUseCase.execute(this)
    }

    fun addMarker(marker: Marker) {
        viewModelScope.launch { addMarkerUseCase.execute(marker) }
    }

    override fun onDataChanged() {
        viewModelScope.launch {
            _markers.postValue(getMarkersUseCase.execute())
        }
    }
}