package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.ui.observers.MarkersObserver

interface AddObserverUseCase {
    fun execute(observer: MarkersObserver)
}