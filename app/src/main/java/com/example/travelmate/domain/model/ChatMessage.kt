package com.example.travelmate.domain.model

data class ChatMessage(
    val uid_from: String = "",
    val uid_to: String = "",
    val fullName: String = "",
    val message: String = "",
    val timestamp: Long = 0,
)
