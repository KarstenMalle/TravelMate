package com.example.travelmate.ui.forgot_pw

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelmate.core.Utils.Companion.showMessage
import com.example.travelmate.ui.forgot_pw.components.ForgotPassword
import com.example.travelmate.ui.forgot_pw.components.ForgotPasswordContent
import com.example.travelmate.ui.forgot_pw.components.ForgotPasswordTopBar

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ForgotPasswordTopBar(
                navigateBack = navigateBack
            )
        },
        content = { padding ->
            ForgotPasswordContent(
                padding = padding,
                sendPasswordResetEmail = { email ->
                    viewModel.sendPasswordResetEmail(email)
                }
            )
        }
    )

    ForgotPassword(
        navigateBack = navigateBack,
        showResetPasswordMessage = {
            showMessage(context, "Reset password")
        },
        showErrorMessage = { errorMessage ->
            showMessage(context, errorMessage)
        }
    )
}