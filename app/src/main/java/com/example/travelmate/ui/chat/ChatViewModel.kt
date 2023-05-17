package com.example.travelmate.ui.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.domain.model.Chat
import com.example.travelmate.domain.model.ChatMessage
import com.example.travelmate.domain.model.Response
import com.example.travelmate.domain.model.UserProfile
import com.example.travelmate.domain.repository.AuthRepository
import com.example.travelmate.domain.repository.UserRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var friends by mutableStateOf<List<UserProfile>>(emptyList())
        private set

    var friendsLoading by mutableStateOf<Response<Boolean>>(Response.Loading)
        private set

    private var lastFriendFullName: String? = null

    fun loadFriends(limit: Int) = viewModelScope.launch {
        friendsLoading = Response.Loading
        try {
            val uid = getCurrentUserUid()
            if (uid != null) {
                val newFriends = userRepository.getFriends(uid, lastFriendFullName, limit)
                if (newFriends.isNotEmpty()) {
                    lastFriendFullName = newFriends.last().fullName
                }
                friends = newFriends
            }
            friendsLoading = Response.Success(true)
        } catch (e: FirebaseFirestoreException) {
            friendsLoading = Response.Failure(e)
        }
    }

    var friendProfile by mutableStateOf<UserProfile?>(null)
        private set

    fun getFriendProfile(friendUid: String) = viewModelScope.launch {
        try {
            friendProfile = userRepository.getUserProfile(friendUid)
        } catch (e: FirebaseFirestoreException) {
            // Handle error
        }
    }

    private var chatError by mutableStateOf<String?>(null)

    private var chatLoading by mutableStateOf<Response<Boolean>>(Response.Loading)

    var messages by mutableStateOf<List<ChatMessage>>(emptyList())
        private set

    fun loadMessages(friendUid: String) = viewModelScope.launch {
        chatLoading = Response.Loading
        try {
            val uid = getCurrentUserUid()
            if (uid != null) {
                val newMessages = userRepository.getChatMessages(uid, friendUid)
                messages = newMessages
            }
            chatLoading = Response.Success(true)
        } catch (e: FirebaseFirestoreException) {
            chatLoading = Response.Failure(e)
        }
    }

    fun sendMessage(toUid: String, message: String, fullName: String, navigateBack: () -> Unit) = viewModelScope.launch {
        try {
            val uid = getCurrentUserUid()
            if (uid != null) {
                val friendProfile = userRepository.getUserProfile(toUid)
                val timestamp = System.currentTimeMillis()
                val chatMessage = ChatMessage(
                    uid_from = uid,
                    uid_to = toUid,
                    fullName = fullName,
                    message = message,
                    timestamp = timestamp
                )
                val date = Date(timestamp)
                val chatInfo = Chat(
                    id = toUid,
                    lastMessage = message,
                    lastMessageTimestamp = Timestamp(date),
                    fullName = fullName,
                    photoUrl = friendProfile?.photoUrl
                )
                userRepository.sendMessage(chatMessage, chatInfo)
                loadMessages(toUid)  // Reload messages after sending a new one
                navigateBack()
            } else {
                chatError = "Failed to send message. User is not logged in."
            }
        } catch (e: FirebaseFirestoreException) {
            chatError = "Failed to send message. Check your internet connection and try again."
        }
    }

    fun signOut() = authRepository.signOut()


    fun getCurrentUserUid(): String? {
        return authRepository.currentUser?.uid
    }
}
