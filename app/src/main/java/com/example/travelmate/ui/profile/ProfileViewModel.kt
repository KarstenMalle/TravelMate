package com.example.travelmate.ui.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.domain.model.Response
import com.example.travelmate.domain.model.Response.Loading
import com.example.travelmate.domain.model.Response.Success
import com.example.travelmate.domain.model.UserProfile
import com.example.travelmate.domain.repository.AuthRepository
import com.example.travelmate.domain.repository.ReloadUserResponse
import com.example.travelmate.domain.repository.RevokeAccessResponse
import com.example.travelmate.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
): ViewModel() {
    var revokeAccessResponse by mutableStateOf<RevokeAccessResponse>(Success(false))
        private set
    var reloadUserResponse by mutableStateOf<ReloadUserResponse>(Success(false))
        private set

    fun reloadUser() = viewModelScope.launch {
        reloadUserResponse = Loading
        reloadUserResponse = authRepository.reloadFirebaseUser()
    }

    val isEmailVerified get() = authRepository.currentUser?.isEmailVerified ?: false

    fun signOut() = authRepository.signOut()

    fun revokeAccess() = viewModelScope.launch {
        revokeAccessResponse = Loading
        revokeAccessResponse = authRepository.revokeAccess()
    }

    var userProfile by mutableStateOf<UserProfile?>(null)
        private set

    var userProfileError by mutableStateOf<String?>(null)
        private set

    var userProfileLoading by mutableStateOf<Response<Boolean>>(Response.Loading)
        private set

    fun loadUserProfile(uid: String) = viewModelScope.launch {
        userProfileLoading = Response.Loading
        try {
            userProfile = userRepository.getUserProfile(uid)
            userProfileLoading = Response.Success(true)
        } catch (e: FirebaseFirestoreException) {
            userProfileError = "Failed to load user profile. Check your internet connection and try again."
            userProfileLoading = Response.Failure(e)
        }
    }


    fun updateUserProfile(updatedProfile: UserProfile) = viewModelScope.launch {
        try {
            userProfile = updatedProfile
            userRepository.updateUserProfile(updatedProfile)
        } catch (e: FirebaseFirestoreException) {
            userProfileError = "Failed to update user profile. Check your internet connection and try again."
        }
    }




    fun getCurrentUserUid(): String? {
        return authRepository.currentUser?.uid
    }

    fun updateFullName(fullName: String) {
        userProfile?.let {
            userProfile = it.copy(fullName = fullName)
        }
    }

    private val storage = Firebase.storage

    var photoUrlLoading by mutableStateOf<Response<Boolean>>(Response.Loading)
        private set

    fun updatePhotoUrl(photoUri: Uri?) = viewModelScope.launch {
        photoUrlLoading = Response.Loading
        try {
            photoUri?.let {
                val url = uploadPhotoAndGetUrl(it)
                userProfile?.let { profile ->
                    val updatedProfile = profile.copy(photoUrl = url)
                    userProfile = updatedProfile
                    userRepository.updateUserProfile(updatedProfile)
                }
                photoUrlLoading = Response.Success(true)
            }
        } catch (e: Exception) {
            photoUrlLoading = Response.Failure(e)
        }
    }

    private suspend fun uploadPhotoAndGetUrl(photoUri: Uri): String = withContext(Dispatchers.IO) {
        val userId = authRepository.currentUser?.uid
        val photoRef = storage.reference.child("users/$userId/images/${photoUri.lastPathSegment}")
        val uploadTask = photoRef.putFile(photoUri)

        // Await the completion of the upload task
        uploadTask.await()

        // Get the download URL
        photoRef.downloadUrl.await().toString()
    }

    fun updateAge(age: String) {
        userProfile?.let {
            userProfile = it.copy(age = age.toIntOrNull() ?: 0)
        }
    }

    fun updateHomeCountry(homeCountry: String) {
        userProfile?.let {
            userProfile = it.copy(homeCountry = homeCountry)
        }
    }

    fun updateBio(bio: String) {
        userProfile?.let {
            userProfile = it.copy(bio = bio)
        }
    }


    fun saveUserProfile() = viewModelScope.launch {
        userProfile?.let {
            userRepository.updateUserProfile(it)
        }
    }


}
