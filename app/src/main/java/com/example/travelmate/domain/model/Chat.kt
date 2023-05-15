package com.example.travelmate.domain.model

import com.google.firebase.Timestamp

data class Chat(
    val id: String = "",
    val lastMessage: String = "",
    val lastMessageTimestamp: Timestamp? = null,
    var fullName: String = "",
    var photoUrl: String? = null,
)
