package com.example.travelmate.ui.friends

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.domain.model.Response
import com.example.travelmate.domain.model.UserProfile
import com.example.travelmate.domain.repository.AuthRepository
import com.example.travelmate.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var friends by mutableStateOf<List<UserProfile>>(emptyList())
        private set

    var friendsError by mutableStateOf<String?>(null)
        private set

    var friendsLoading by mutableStateOf<Response<Boolean>>(Response.Loading)
        private set

    fun signOut() = authRepository.signOut()

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


    fun addFriend(friendUid: String, navigateBack: () -> Unit) = viewModelScope.launch {
        try {
            val uid = getCurrentUserUid()
            if (uid != null) {
                userRepository.addFriend(uid, friendUid)
                loadFriends(10)  // Reload friends after adding new friend
                navigateBack()
            } else {
                friendsError = "Failed to add friend. User is not logged in."
            }
        } catch (e: FirebaseFirestoreException) {
            friendsError = "Failed to add friend. Check your internet connection and try again."
        }
    }

    var searchResults by mutableStateOf<List<UserProfile>>(emptyList())
        private set

    fun searchFriends(query: String) = viewModelScope.launch {
        try {
            searchResults = userRepository.searchUsers(query)
        } catch (e: FirebaseFirestoreException) {
            // Handle error
        }
    }


    private fun getCurrentUserUid(): String? {
        return authRepository.currentUser?.uid
    }
}
