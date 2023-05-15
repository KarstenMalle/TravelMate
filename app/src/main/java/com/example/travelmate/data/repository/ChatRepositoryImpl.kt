package com.example.travelmate.data.repository

import com.example.travelmate.domain.model.Chat
import com.example.travelmate.domain.model.ChatMessage
import com.example.travelmate.domain.repository.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ChatRepository {

    override suspend fun getChats(uid: String): List<Chat> {
        val chatDocs = firestore.collection("users").document(uid).collection("chats")
            .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
            .get()
            .await()

        val chats = chatDocs.documents.mapNotNull { doc ->
            doc.toObject(Chat::class.java)
        }

        return chats
    }

    override suspend fun getMessages(uid: String, friendId: String): List<ChatMessage> {
        val messageDocs = firestore.collection("users").document(uid).collection("chats")
            .document(friendId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .await()

        return messageDocs.documents.mapNotNull { doc ->
            doc.toObject(ChatMessage::class.java)
        }
    }




}