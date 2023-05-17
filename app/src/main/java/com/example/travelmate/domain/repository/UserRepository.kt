package com.example.travelmate.domain.repository

import com.example.travelmate.domain.model.Chat
import com.example.travelmate.domain.model.ChatMessage
import com.example.travelmate.domain.model.UserProfile

interface UserRepository {
    suspend fun getUserProfile(uid: String): UserProfile?
    suspend fun updateUserProfile(userProfile: UserProfile)

    suspend fun getFriends(uid: String, startAfter: String?, limit: Int): List<UserProfile>
    suspend fun addFriend(uid: String, friendUid: String)

    suspend fun getFriendUids(uid: String): List<String>

    suspend fun searchUsers(query: String, uid: String?): List<UserProfile>

    suspend fun getChatMessages(uid: String, friendUid: String): List<ChatMessage>
    suspend fun sendMessage(message: ChatMessage, chatInfo: Chat)

}
