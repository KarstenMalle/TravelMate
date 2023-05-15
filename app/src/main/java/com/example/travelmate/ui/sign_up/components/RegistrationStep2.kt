package com.example.travelmate.ui.sign_up.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun RegistrationStep2(
    onSubmitClicked: (List<String>, String) -> Unit
) {
    var interests by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Step 2: Interests, Bio and Profile Picture")

        OutlinedTextField(
            value = interests,
            onValueChange = { interests = it },
            label = { Text("Interests (comma-separated)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth()
        )


        Button(onClick = {
            val interestsList = interests.split(",").map { it.trim() }
            onSubmitClicked(interestsList, bio)
        }) {
            Text("Submit")
        }
    }
}
