package com.example.travelmate.ui.edit_profile.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun EditProfileForm(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    age: String,
    onAgeChange: (String) -> Unit,
    homeCountry: String,
    onHomeCountryChange: (String) -> Unit,
    bio: String,
    onBioChange: (String) -> Unit,
    photoUrl: String?,
    onPhotoChange: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val pickImageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
        onPhotoChange(uri)
    }

    Column(modifier = modifier) {

        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            label = { Text("Full name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { /* Focus on next field */ })
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = age,
            onValueChange = onAgeChange,
            label = { Text("Age") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { /* Focus on next field */ })
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = homeCountry,
            onValueChange = onHomeCountryChange,
            label = { Text("Home Country") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { /* Focus on next field */ })
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = bio,
            onValueChange = onBioChange,
            label = { Text("Bio") },
            singleLine = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { /* Close the keyboard */ })
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { pickImageLauncher.launch("image/*") }) {
            Text("Pick an Image")
        }

        imageUri?.let {
            Image(
                painter = rememberImagePainter(it),
                contentDescription = "User picked image",
                modifier = Modifier .size(100.dp)
            )
        } ?: photoUrl?.let {
            Image(
                painter = rememberImagePainter(it),
                contentDescription = "User profile image",
                modifier = Modifier .size(100.dp)
            )
        }
    }
}
