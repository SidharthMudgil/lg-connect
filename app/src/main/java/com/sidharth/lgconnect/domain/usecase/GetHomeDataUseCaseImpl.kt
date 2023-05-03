package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.domain.model.HomeData
import com.sidharth.lgconnect.domain.repository.DataRepository

class GetHomeDataUseCaseImpl(private val repository: DataRepository) : GetHomeDataUseCase {
    override fun execute(): HomeData {
        return HomeData(
            planets = repository.getAllPlanets(),
            wonders = repository.getAllWonders(),
            charts = repository.getAllCharts(),
        )
    }
}