package com.sidharth.lgconnect.domain.usecase

interface SetConnectionStatusUseCase {
    fun execute(connectionStatus: String)
}