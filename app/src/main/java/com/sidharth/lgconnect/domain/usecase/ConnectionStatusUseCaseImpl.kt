package com.sidharth.lgconnect.domain.usecase

import com.sidharth.lgconnect.data.repository.ConnectionRepositoryImpl


class ConnectionStatusUseCaseImpl(private val repository: ConnectionRepositoryImpl): GetConnectionStatusUseCase, SetConnectionStatusUseCase {
    override suspend fun execute(): String {
        return repository.getConnectionStatus()
    }

    override fun execute(connectionStatus: String) {
        repository.setConnectionStatus(connectionStatus)
    }
}