package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.data.repository.DataRepositoryImpl
import com.sidharth.lgconnect.domain.model.HomeData

class GetHomeDataUseCaseImpl(private val repository: DataRepositoryImpl) : GetHomeDataUseCase {
    override fun execute(): HomeData {
        return HomeData(
            planets = repository.getAllPlanets(),
            wonders = repository.getAllWonders(),
            charts = repository.getAllCharts(),
        )
    }
}