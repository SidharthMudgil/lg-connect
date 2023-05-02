package com.sidharth.lgconnect.ui.maps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.domain.usecase.AddMarkerUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.AddObserverUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.GetMarkersUseCaseImpl
import com.sidharth.lgconnect.ui.observers.MarkersObserver
import kotlinx.coroutines.launch

class MapsViewModel(
    private val getMarkersUseCaseImpl: GetMarkersUseCaseImpl,
    private val addMarkerUseCaseImpl: AddMarkerUseCaseImpl,
    addObserverUseCaseImpl: AddObserverUseCaseImpl
) : ViewModel(), MarkersObserver {

    private val _markers = MutableLiveData<MutableList<Marker>>()
    val markers: LiveData<MutableList<Marker>>
        get() = _markers

    init {
        viewModelScope.launch {
            _markers.postValue(getMarkersUseCaseImpl.execute())
        }
        addObserverUseCaseImpl.execute(this)
    }

    fun addMarker(marker: Marker) {
        val updatedMarkers = markers.value?.apply { add(marker) }
        updatedMarkers?.let { _markers.value = it }
        viewModelScope.launch { addMarkerUseCaseImpl.execute(marker) }
    }

    override fun onDataChanged() {
        viewModelScope.launch {
            _markers.postValue(getMarkersUseCaseImpl.execute())
        }
    }
}