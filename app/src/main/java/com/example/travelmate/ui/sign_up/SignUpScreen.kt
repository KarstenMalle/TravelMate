package com.example.travelmate.ui.sign_up

import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelmate.core.Utils.Companion.showMessage
import com.example.travelmate.ui.sign_up.components.*

@Composable
@ExperimentalComposeUiApi
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    var step by remember { mutableStateOf(1)}

    Scaffold(
        topBar = {
            SignUpTopBar(
                navigateBack = navigateBack
            )
        },
        content = { padding ->
            when (step) {
                1 -> SignUpContent(
                    padding = padding,
                    signUp = { email, password ->
                        viewModel.setEmailAndPassword(email, password)
                        step = 2
                    },
                    navigateBack = navigateBack
                )
                2 -> RegistrationStep1 { fullName, age, country ->
                    // Pass the values to the viewModel
                    viewModel.setStep1Data(fullName, age, country)
                    step = 3
                }
                3 -> RegistrationStep2 { interests, bio ->
                    // Pass the values to the viewModel
                    viewModel.setStep2Data(interests, bio)
                    viewModel.signUpWithEmailAndPassword()
                }
            }
        }
    )

    SignUp(
        sendEmailVerification = {
            viewModel.sendEmailVerification()
        },
        showVerifyEmailMessage = {
            showMessage(context, "Please verify your email")
        }
    )

    SendEmailVerification()
}