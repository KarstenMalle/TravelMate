package com.example.travelmate.ui.login.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelmate.ui.components.ProgressBar
import com.example.travelmate.domain.model.Response.*
import com.example.travelmate.ui.login.LoginViewModel
import com.example.travelmate.core.Utils.Companion.print

@Composable
fun Login(
    viewModel: LoginViewModel = hiltViewModel(),
    showErrorMessage: (errorMessage: String?) -> Unit
) {
    when(val signInResponse = viewModel.signInResponse) {
        is Loading -> ProgressBar()
        is Success -> Unit
        is Failure -> signInResponse.apply {
            LaunchedEffect(e) {
                print(e)
                showErrorMessage(e.message)
            }
        }
    }
}
