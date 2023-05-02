package com.sidharth.lgconnect.domain.usecase

interface GetConnectionStatusUseCase {
    suspend fun execute(): String
}