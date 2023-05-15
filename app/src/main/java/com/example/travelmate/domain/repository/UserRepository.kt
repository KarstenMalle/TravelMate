package com.example.travelmate.domain.repository

import com.example.travelmate.domain.model.ChatMessage
import com.example.travelmate.domain.model.UserProfile

interface UserRepository {
    suspend fun getUserProfile(uid: String): UserProfile?
    suspend fun updateUserProfile(userProfile: UserProfile)

    suspend fun getFriends(uid: String, startAfter: String?, limit: Int): List<UserProfile>
    suspend fun addFriend(uid: String, friendUid: String)

    suspend fun searchUsers(query: String): List<UserProfile>

    suspend fun getChatMessages(uid: String, chatId: String): List<ChatMessage>
    suspend fun sendMessage(uid: String, chatId: String, message: ChatMessage)
}
