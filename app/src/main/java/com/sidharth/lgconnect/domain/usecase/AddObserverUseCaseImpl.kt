package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.domain.repository.DataRepository
import com.sidharth.lgconnect.ui.observers.MarkersObserver

class AddObserverUseCaseImpl(private val repository: DataRepository) : AddObserverUseCase {
    override fun execute(observer: MarkersObserver) {
        repository.addObserver(observer)
    }
}