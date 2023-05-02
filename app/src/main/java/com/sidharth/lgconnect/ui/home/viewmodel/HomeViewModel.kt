package com.sidharth.lgconnect.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidharth.lgconnect.domain.model.HomeData
import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.domain.usecase.DeleteMarkerUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.GetHomeDataUseCaseImpl
import com.sidharth.lgconnect.domain.usecase.GetMarkersUseCaseImpl
import kotlinx.coroutines.launch

class HomeViewModel(
    getHomeDataUseCaseImpl: GetHomeDataUseCaseImpl,
    private val getMarkersUseCaseImpl: GetMarkersUseCaseImpl,
    private val deleteMarkerUseCaseImpl: DeleteMarkerUseCaseImpl
) : ViewModel() {

    private val _homeData = getHomeDataUseCaseImpl.execute()
    val homeData: HomeData get() = _homeData

    private val _markers = MutableLiveData<MutableList<Marker>>()
    val markers: LiveData<MutableList<Marker>>
        get() = _markers

    init {
        viewModelScope.launch {
            _markers.postValue(getMarkersUseCaseImpl.execute())
        }
    }

    fun deleteMarker(marker: Marker) {
        val updatedMarkers = markers.value?.apply { remove(marker) }
        updatedMarkers?.let { _markers.value = it }
        viewModelScope.launch { deleteMarkerUseCaseImpl.execute(marker) }
    }
}

