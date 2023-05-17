package com.example.travelmate.ui.chat.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.domain.model.Chat
import com.example.travelmate.domain.repository.AuthRepository
import com.example.travelmate.domain.repository.ChatRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ChatOverviewViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _chats = MutableLiveData<List<Chat>>()
    val chats: LiveData<List<Chat>> = _chats

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadChats()
    }

    fun loadChats() {
        viewModelScope.launch {
            _isLoading.value = true
            val uid = authRepository.currentUser?.uid
            if (uid != null) {
                try {
                    _chats.value = chatRepository.getChats(uid)
                } catch (e: Exception) {
                    _errorMessage.value = "Failed to load chats: ${e.localizedMessage}"
                }
            }
            _isLoading.value = false
        }
    }


    fun getFormattedTime(timestamp: Timestamp?): String {
        if (timestamp == null) return "Unknown"

        val now = System.currentTimeMillis()
        val diffInSeconds = (now - timestamp.toDate().time) / 1000

        return when {
            diffInSeconds < 60 -> "$diffInSeconds seconds ago"
            diffInSeconds < 60 * 60 -> "${diffInSeconds / 60} minutes ago"
            diffInSeconds < 60 * 60 * 24 -> "${diffInSeconds / (60 * 60)} hours ago"
            else -> SimpleDateFormat("d. MMM", Locale.getDefault()).format(timestamp.toDate())
        }
    }
}
