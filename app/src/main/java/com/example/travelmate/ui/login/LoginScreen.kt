package com.example.travelmate.ui.login

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelmate.core.Utils.Companion.showMessage
import com.example.travelmate.ui.login.components.Login
import com.example.travelmate.ui.login.components.LoginContent
import com.example.travelmate.ui.login.components.LoginTopBar

@Composable
@ExperimentalComposeUiApi
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToForgotPasswordScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            LoginTopBar()
        },
        content = { padding ->
            LoginContent(
                padding = padding,
                signIn = { email, password ->
                    viewModel.signInWithEmailAndPassword(email, password)
                },
                navigateToForgotPasswordScreen = navigateToForgotPasswordScreen,
                navigateToSignUpScreen = navigateToSignUpScreen
            )
        }
    )

    Login(
        showErrorMessage = { errorMessage ->
            showMessage(context, errorMessage)
        }
    )
}