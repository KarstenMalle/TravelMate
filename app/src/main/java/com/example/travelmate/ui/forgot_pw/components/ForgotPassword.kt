package com.example.travelmate.ui.forgot_pw.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelmate.ui.components.ProgressBar
import com.example.travelmate.core.Utils.Companion.print
import com.example.travelmate.domain.model.Response.*
import com.example.travelmate.ui.forgot_pw.ForgotPasswordViewModel

@Composable
fun ForgotPassword(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    showResetPasswordMessage: () -> Unit,
    showErrorMessage: (errorMessage: String?) -> Unit
) {
    when(val sendPasswordResetEmailResponse = viewModel.sendPasswordResetEmailResponse) {
        is Loading -> ProgressBar()
        is Success -> {
            val isPasswordResetEmailSent = sendPasswordResetEmailResponse.data
            LaunchedEffect(isPasswordResetEmailSent) {
                if (isPasswordResetEmailSent) {
                    navigateBack()
                    showResetPasswordMessage()
                }
            }
        }
        is Failure -> sendPasswordResetEmailResponse.apply {
            LaunchedEffect(e) {
                print(e)
                showErrorMessage(e.message)
            }
        }
    }
}