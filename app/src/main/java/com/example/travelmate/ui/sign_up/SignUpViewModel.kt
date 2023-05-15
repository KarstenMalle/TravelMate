package com.example.travelmate.ui.sign_up

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.domain.model.Response.Loading
import com.example.travelmate.domain.model.Response.Success
import com.example.travelmate.domain.model.UserProfile
import com.example.travelmate.domain.repository.AuthRepository
import com.example.travelmate.domain.repository.SendEmailVerificationResponse
import com.example.travelmate.domain.repository.SignUpResponse
import com.example.travelmate.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    private var documentCreationError by mutableStateOf<String?>(null)

    var signUpResponse by mutableStateOf<SignUpResponse>(Success(false))
        private set
    var sendEmailVerificationResponse by mutableStateOf<SendEmailVerificationResponse>(Success(false))
        private set

    // Add new mutable state values to store data from Step1, Step2, and Step3
    var email by mutableStateOf<String?>(null)
    var password by mutableStateOf<String?>(null)
    var fullName by mutableStateOf<String?>(null)
    var age by mutableStateOf<Int?>(null)
    var country by mutableStateOf<String?>(null)
    var interests by mutableStateOf<List<String>?>(null)
    var bio by mutableStateOf<String?>(null)
    var imageUri by mutableStateOf<Uri?>(null)

    // Add new functions to store data from each step
    fun setEmailAndPassword(email: String, password: String) {
        this.email = email
        this.password = password
    }

    fun setStep1Data(fullName: String, age: Int, country: String) {
        this.fullName = fullName
        this.age = age
        this.country = country
    }

    fun setStep2Data(interests: List<String>, bio: String) {
        this.interests = interests
        this.bio = bio
    }

    fun signUpWithEmailAndPassword() = viewModelScope.launch {
        if (email != null && password != null && fullName != null && age != null && country != null && interests != null && bio != null) {
            signUpResponse = Loading
            signUpResponse = authRepository.firebaseSignUpWithEmailAndPassword(email!!, password!!)

            if (signUpResponse is Success) {
                authRepository.currentUser?.uid?.let { uid ->
                    viewModelScope.launch {
                        // Use the provided default image URL for new users.
                        val photoUrl = "https://firebasestorage.googleapis.com/v0/b/travelmate-356eb.appspot.com/o/default%2Fdefault-profile-image.png?alt=media&token=7a881272-7de6-4c8f-8088-7124a7931194"

                        val userProfile = UserProfile(
                            uid = uid,
                            fullName = fullName!!,
                            email = email!!,
                            photoUrl = photoUrl, // Set this to the uploaded photo URL
                            age = age!!,
                            homeCountry = country!!,
                            interests = interests!!,
                            bio = bio!!
                        )

                        val createUserDocumentDeferred = async { createUserDocument(userProfile) }
                        val sendEmailVerificationDeferred = async { sendEmailVerification() }

                        createUserDocumentDeferred.await()
                        sendEmailVerificationDeferred.await()
                    }
                }
            }
        }
    }

    private suspend fun uploadProfilePicture(uid: String, uri: Uri): String? {
        val imageRef = storage.reference.child("profile_pictures/$uid")
        return try {
            imageRef.putFile(uri).await()
            imageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("SignUpViewModel", "Error uploading profile picture: ${e.message}")
            null
        }
    }

    private fun createUserDocument(userProfile: UserProfile) {
        val userDocument = userProfile.toHashMap()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("SignUpViewModel", "Creating user document for UID: ${userProfile.uid}")
                firestore.collection("users").document(userProfile.uid).set(userDocument).await()
                Log.d("SignUpViewModel", "User document created successfully")
            } catch (e: Exception) {
                // Handle any errors that might occur during document creation
                documentCreationError = e.message
                Log.e("SignUpViewModel", "Error creating user document: ${e.message}")
            }
        }
    }


    fun sendEmailVerification() = viewModelScope.launch {
        sendEmailVerificationResponse = Loading
        sendEmailVerificationResponse = authRepository.sendEmailVerification()
    }
}
