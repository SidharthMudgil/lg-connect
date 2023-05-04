package com.sidharth.lgconnect.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidharth.lgconnect.domain.model.HomeData
import com.sidharth.lgconnect.domain.model.Marker
import com.sidharth.lgconnect.domain.usecase.AddObserverUseCase
import com.sidharth.lgconnect.domain.usecase.DeleteMarkerUseCase
import com.sidharth.lgconnect.domain.usecase.GetHomeDataUseCase
import com.sidharth.lgconnect.domain.usecase.GetMarkersUseCase
import com.sidharth.lgconnect.ui.observer.MarkersObserver
import kotlinx.coroutines.launch

class HomeViewModel(
    getHomeDataUseCase: GetHomeDataUseCase,
    addObserverUseCaseImpl: AddObserverUseCase,
    private val getMarkersUseCase: GetMarkersUseCase,
    private val deleteMarkerUseCase: DeleteMarkerUseCase,
) : ViewModel(), MarkersObserver {

    private val _homeData = getHomeDataUseCase.execute()
    val homeData: HomeData get() = _homeData

    private val _markers = MutableLiveData<MutableList<Marker>>()
    val markers: LiveData<MutableList<Marker>>
        get() = _markers

    init {
        viewModelScope.launch {
            _markers.postValue(getMarkersUseCase.execute())
        }
        addObserverUseCaseImpl.execute(this)
    }

    fun deleteMarker(marker: Marker) {
        viewModelScope.launch { deleteMarkerUseCase.execute(marker) }
//        val updatedMarkers = markers.value?.apply { remove(marker) }
//        updatedMarkers?.let { _markers.postValue(it) }
    }

    override fun onDataChanged() {
        viewModelScope.launch {
            _markers.postValue(getMarkersUseCase.execute())
        }
    }
}

