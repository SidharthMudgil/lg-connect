package com.sidharth.lgconnect.domain.model

data class Wonder(
    val cover: Int,
    val title: String,
    val subtitle: String,
    val description: String,
    val address: String,
    val city: String,
    val state: String,
    val country: String,
    val pin: String,
)