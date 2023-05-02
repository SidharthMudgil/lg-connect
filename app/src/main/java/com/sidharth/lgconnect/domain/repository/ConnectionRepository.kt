package com.sidharth.lgconnect.domain.repository

interface ConnectionRepository {
    fun getConnectionStatus(): String
    fun setConnectionStatus(status: String)
}