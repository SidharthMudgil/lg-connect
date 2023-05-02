package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.domain.model.HomeData

interface GetHomeDataUseCase {
    fun execute(): HomeData
}