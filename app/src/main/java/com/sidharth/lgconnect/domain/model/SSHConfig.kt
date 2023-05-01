package com.sidharth.lgconnect.domain.model

data class SSHConfig(
    val username: String,
    val password: String,
    val hostname: String,
    val port: Int,
)
