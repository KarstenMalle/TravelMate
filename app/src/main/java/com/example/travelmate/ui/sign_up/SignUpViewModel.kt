package com.example.travelmate.ui.sign_up

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
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestore: FirebaseFirestore,
) : ViewModel() {

    private var documentCreationError by mutableStateOf<String?>(null)

    var signUpResponse by mutableStateOf<SignUpResponse>(Success(false))
        private set
    var sendEmailVerificationResponse by mutableStateOf<SendEmailVerificationResponse>(Success(false))
        private set

    var email by mutableStateOf<String?>(null)
    var password by mutableStateOf<String?>(null)
    var fullName by mutableStateOf<String?>(null)
    private var age by mutableStateOf<Int?>(null)
    private var country by mutableStateOf<String?>(null)
    private var interests by mutableStateOf<List<String>?>(null)
    private var bio by mutableStateOf<String?>(null)

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
                        val photoUrl = "https://firebasestorage.googleapis.com/v0/b/travelmate-356eb.appspot.com/o/default%2Fdefault-profile-image.png?alt=media&token=7a881272-7de6-4c8f-8088-7124a7931194"

                        val userProfile = UserProfile(
                            uid = uid,
                            fullName = fullName!!,
                            email = email!!,
                            photoUrl = photoUrl,
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

    private fun createUserDocument(userProfile: UserProfile) {
        val userDocument = userProfile.toHashMap()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("SignUpViewModel", "Creating user document for UID: ${userProfile.uid}")
                firestore.collection("users").document(userProfile.uid).set(userDocument).await()
                Log.d("SignUpViewModel", "User document created successfully")
            } catch (e: Exception) {
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
