package com.example.travelmate.domain.repository

import com.example.travelmate.domain.model.Chat

interface ChatRepository {
    suspend fun getChats(uid: String): List<Chat>

}