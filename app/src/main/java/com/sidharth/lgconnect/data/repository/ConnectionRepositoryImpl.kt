package com.sidharth.lgconnect.data.repository

import com.sidharth.lgconnect.domain.repository.ConnectionRepository

class ConnectionRepositoryImpl : ConnectionRepository {
    private var connectionStatus: String = "disconnected"

    override fun getConnectionStatus(): String {
        return connectionStatus
    }

    override fun setConnectionStatus(status: String) {
        connectionStatus = status
    }
}