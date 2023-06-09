package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.ui.observer.MarkersObserver

interface AddObserverUseCase {
    fun execute(observer: MarkersObserver)
}