package com.example.travelmate.ui.edit_profile.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalComposeUiApi::class)
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
    interests: String,
    onInterestsChange: (String) -> Unit,
    photoUrl: String?,
    onPhotoChange: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val pickImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            onPhotoChange(uri)
        }
    val fullNameFocusRequester = remember { FocusRequester() }
    val ageFocusRequester = remember { FocusRequester() }
    val homeCountryFocusRequester = remember { FocusRequester() }
    val bioFocusRequester = remember { FocusRequester() }
    val interestsFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Current profile picture",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "User picked image",
                modifier = Modifier.size(300.dp)
            )
        } ?: photoUrl?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "User profile image",
                modifier = Modifier.size(300.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { pickImageLauncher.launch("image/*") }) {
            Text("Select a new picture")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            label = { Text("Full name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { ageFocusRequester.requestFocus() }),
            modifier = Modifier.focusRequester(fullNameFocusRequester)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = age,
            onValueChange = onAgeChange,
            label = { Text("Age") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { homeCountryFocusRequester.requestFocus() }),
            modifier = Modifier.focusRequester(ageFocusRequester)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = homeCountry,
            onValueChange = onHomeCountryChange,
            label = { Text("Home Country") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { bioFocusRequester.requestFocus() }),
            modifier = Modifier.focusRequester(homeCountryFocusRequester)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = bio,
            onValueChange = onBioChange,
            label = { Text("Bio") },
            singleLine = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { interestsFocusRequester.requestFocus() }),
            modifier = Modifier
                .widthIn(max = 280.dp)
                .focusRequester(bioFocusRequester)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = interests,
            onValueChange = onInterestsChange,
            label = { Text("Interests") },
            singleLine = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            modifier = Modifier
                .widthIn(max = 280.dp)
                .focusRequester(interestsFocusRequester)
        )

    }
}
