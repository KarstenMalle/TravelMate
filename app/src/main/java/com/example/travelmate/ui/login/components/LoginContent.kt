package com.example.travelmate.ui.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelmate.ui.components.EmailField
import com.example.travelmate.ui.components.PasswordField
import com.example.travelmate.ui.components.SmallSpacer

@Composable
@ExperimentalComposeUiApi
fun LoginContent(
    padding: PaddingValues,
    signIn: (email: String, password: String) -> Unit,
    navigateToForgotPasswordScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit
) {
    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }
    var password by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue("")) }
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(
            email = email,
            onEmailValueChange = { newValue ->
                email = newValue
            }
        )
        SmallSpacer()
        PasswordField(
            password = password,
            onPasswordValueChange = { newValue ->
                password = newValue
            }
        )
        SmallSpacer()
        Button(
            onClick = {
                keyboard?.hide()
                signIn(email.text, password.text)
            }
        ) {
            Text(
                text = "Login",
                fontSize = 15.sp
            )
        }
        Row {
            Text(
                modifier = Modifier.clickable {
                    navigateToForgotPasswordScreen()
                },
                text = "Forgot password?",
                fontSize = 15.sp
            )
            Text(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                text = "|",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.clickable {
                    navigateToSignUpScreen()
                },
                text = "No account? Sign up.",
                fontSize = 15.sp
            )
        }
    }
}