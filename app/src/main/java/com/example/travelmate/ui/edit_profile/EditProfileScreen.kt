package com.example.travelmate.ui.edit_profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelmate.core.Utils.Companion.showMessage
import com.example.travelmate.domain.model.Response
import com.example.travelmate.ui.edit_profile.components.EditProfileForm
import com.example.travelmate.ui.profile.ProfileViewModel


@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val userId = viewModel.getCurrentUserUid() ?: return
    val userProfile = viewModel.userProfile
    val userProfileError = viewModel.userProfileError
    val userProfileLoading = viewModel.userProfileLoading

    LaunchedEffect(userId) {
        viewModel.loadUserProfile(userId)
    }

    userProfileError?.let {
        showMessage(context, it)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            when (userProfileLoading) {
                is Response.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is Response.Failure -> {
                    Text("Error loading profile")
                }
                is Response.Success -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            userProfile?.let { it ->
                                EditProfileForm(
                                    fullName = it.fullName,
                                    onFullNameChange = { viewModel.updateFullName(it) },
                                    age = it.age.toString(),
                                    onAgeChange = { viewModel.updateAge(it) },
                                    homeCountry = it.homeCountry,
                                    onHomeCountryChange = { viewModel.updateHomeCountry(it) },
                                    bio = it.bio,
                                    onBioChange = { viewModel.updateBio(it) },
                                    interests = it.interests.joinToString(", "),
                                    onInterestsChange = {
                                        viewModel.updateInterests(
                                            it.split(", ").toList()
                                        )
                                    },
                                    photoUrl = it.photoUrl,
                                    onPhotoChange = { viewModel.updatePhotoUrl(it) },
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(32.dp),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Button(onClick = {
                                    userProfile?.let { viewModel.updateUserProfile(it) }
                                    navigateBack()
                                }) {
                                    Text("Save")
                                }

                                Button(onClick = navigateBack) {
                                    Text("Cancel")
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                        }
                    }
                }
            }
        }
    )
}