package com.example.travelmate.ui.sign_up.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun RegistrationStep1(
    onNext: (String, Int, String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        val (fullName, setFullName) = remember { mutableStateOf("") }
        val (age, setAge) = remember { mutableStateOf("") }
        val (country, setCountry) = remember { mutableStateOf("") }

        OutlinedTextField(
            value = fullName,
            onValueChange = setFullName,
            label = { Text("Full Name") },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        OutlinedTextField(
            value = age,
            onValueChange = setAge,
            label = { Text("Age") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = MaterialTheme.typography.body1,
            singleLine = true
        )


        OutlinedTextField(
            value = country,
            onValueChange = setCountry,
            label = { Text("Country") },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Button(
            onClick = {
                val ageAsInt = age.toIntOrNull() ?: 0
                onNext(fullName, ageAsInt, country)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Next")
        }
    }
}
