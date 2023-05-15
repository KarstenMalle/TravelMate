package com.example.travelmate.ui.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.domain.model.ChatMessage
import com.example.travelmate.domain.model.Response
import com.example.travelmate.domain.model.UserProfile
import com.example.travelmate.domain.repository.AuthRepository
import com.example.travelmate.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    var messages by mutableStateOf<List<ChatMessage>>(emptyList())
        private set

    var chatError by mutableStateOf<String?>(null)
        private set

    var chatLoading by mutableStateOf<Response<Boolean>>(Response.Loading)
        private set

    fun loadMessages() = viewModelScope.launch {
        chatLoading = Response.Loading
        try {
            val uid = getCurrentUserUid()
            if (uid != null) {
                // val newMessages = userRepository.getChatMessages(uid)
                // messages = newMessages
            }
            chatLoading = Response.Success(true)
        } catch (e: FirebaseFirestoreException) {
            chatLoading = Response.Failure(e)
        }
    }

    fun sendMessage(toUid: String, message: ChatMessage, navigateBack: () -> Unit) = viewModelScope.launch {
        try {
            val uid = getCurrentUserUid()
            if (uid != null) {
                userRepository.sendMessage(uid, toUid, message)
                loadMessages()  // Reload messages after sending a new one
                navigateBack()
            } else {
                chatError = "Failed to send message. User is not logged in."
            }
        } catch (e: FirebaseFirestoreException) {
            chatError = "Failed to send message. Check your internet connection and try again."
        }
    }

    fun signOut() = authRepository.signOut()


    private fun getCurrentUserUid(): String? {
        return authRepository.currentUser?.uid
    }
}
