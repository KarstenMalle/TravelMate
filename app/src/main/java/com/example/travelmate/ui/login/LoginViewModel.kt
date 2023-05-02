package com.example.travelmate.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.example.travelmate.domain.model.Response.Loading
import com.example.travelmate.domain.model.Response.Success
import com.example.travelmate.domain.repository.AuthRepository
import com.example.travelmate.domain.repository.SignInResponse
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    var signInResponse by mutableStateOf<SignInResponse>(Success(false))
        private set

    fun signInWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        signInResponse = Loading
        signInResponse = repo.firebaseSignInWithEmailAndPassword(email, password)
    }
}