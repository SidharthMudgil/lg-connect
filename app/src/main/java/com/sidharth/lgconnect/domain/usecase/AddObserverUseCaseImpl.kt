package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.data.repository.DataRepositoryImpl
import com.sidharth.lgconnect.ui.observers.MarkersObserver

class AddObserverUseCaseImpl(private val repository: DataRepositoryImpl) {
    fun execute(observer: MarkersObserver) {
        repository.addObserver(observer)
    }
}