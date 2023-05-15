package com.example.travelmate.ui.chat.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.domain.model.Chat
import com.example.travelmate.domain.repository.AuthRepository
import com.example.travelmate.domain.repository.ChatRepository
import com.example.travelmate.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatOverviewViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _chats = MutableLiveData<List<Chat>>()
    val chats: LiveData<List<Chat>> = _chats

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadChats()
    }

    private fun loadChats() {
        viewModelScope.launch {
            _isLoading.value = true
            val uid = authRepository.currentUser?.uid
            if (uid != null) {
                _chats.value = chatRepository.getChats(uid)
            }
            _isLoading.value = false
        }
    }
}
