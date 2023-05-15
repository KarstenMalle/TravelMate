package com.example.travelmate.domain.repository

import com.example.travelmate.domain.model.Chat
import com.example.travelmate.domain.model.ChatMessage

interface ChatRepository {
    suspend fun getChats(uid: String): List<Chat>
    suspend fun getMessages(uid: String, friendId: String): List<ChatMessage>


}