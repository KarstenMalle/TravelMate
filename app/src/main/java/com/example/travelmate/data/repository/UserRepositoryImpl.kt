package com.example.travelmate.data.repository

import com.example.travelmate.domain.model.ChatMessage
import com.example.travelmate.domain.model.UserProfile
import com.example.travelmate.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun getUserProfile(uid: String): UserProfile? {
        val documentSnapshot = firestore.collection("users").document(uid).get().await()
        return if (documentSnapshot.exists()) {
            documentSnapshot.toObject(UserProfile::class.java)
        } else {
            null
        }
    }

    override suspend fun updateUserProfile(userProfile: UserProfile) {
        try {
            firestore.collection("users").document(userProfile.uid).set(userProfile).await()
        } catch (e: Exception) {
            throw FirebaseFirestoreException("Failed to update profile", FirebaseFirestoreException.Code.UNKNOWN, e)
        }
    }

    override suspend fun getFriends(uid: String, startAfter: String?, limit: Int): List<UserProfile> {
        val friendsQuery = firestore.collection("users").document(uid).collection("friends")
            .orderBy("fullName")
            .limit(limit.toLong())

        // If startAfter is not null, start after this friend
        if (startAfter != null) {
            friendsQuery.startAfter(startAfter)
        }

        val friendDocs = friendsQuery.get().await()
        return friendDocs.documents.mapNotNull { doc ->
            doc.getString("uid")?.let { getUserProfile(it) }
        }
    }


    override suspend fun addFriend(uid: String, friendUid: String) {
        val friendProfile = getUserProfile(friendUid)
        if (friendProfile != null) {
            val friendData = mapOf(
                "uid" to friendUid,
                "fullName" to friendProfile.fullName
            )
            firestore.collection("users").document(uid).collection("friends").document(friendUid).set(friendData).await()
        }
    }


    override suspend fun searchUsers(query: String): List<UserProfile> {
        return firestore.collection("users")
            .orderBy("fullName")
            .startAt(query)
            .endAt(query + '\uf8ff')
            .get()
            .await()
            .toObjects(UserProfile::class.java)
    }


    override suspend fun getChatMessages(uid: String, chatId: String): List<ChatMessage> {
        val messageDocs = firestore.collection("users").document(uid).collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp")
            .get()
            .await()

        return messageDocs.documents.mapNotNull { doc ->
            doc.toObject(ChatMessage::class.java)
        }
    }

    override suspend fun sendMessage(uid: String, chatId: String, message: ChatMessage) {
        firestore.collection("users").document(uid).collection("chats").document(chatId).collection("messages")
            .add(message)
            .await()
    }
}
