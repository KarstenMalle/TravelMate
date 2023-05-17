package com.example.travelmate.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelmate.domain.model.Response
import com.example.travelmate.navigation.Screen
import com.example.travelmate.ui.components.BottomBar
import com.example.travelmate.ui.components.TopBar
import com.example.travelmate.ui.profile.components.ProfileContent
import com.example.travelmate.ui.profile.components.RevokeAccess



@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onEditProfile: () -> Unit,
    navigateToFriendsScreen: () -> Unit,
    navigateToChatScreen: () -> Unit,
    navigateToMapScreen: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val userProfileLoading = viewModel.userProfileLoading

    Scaffold(
        topBar = {
            TopBar(
                title = "Profile",
                signOut = {
                    viewModel.signOut()
                },
                revokeAccess = {
                    viewModel.revokeAccess()
                }
            )
        },
        bottomBar = {
            BottomBar(
                currentRoute = Screen.ProfileScreen.route,
                navigateToProfile = {},  // No action needed when already on profile screen
                navigateToMap = navigateToMapScreen,
                navigateToFriends = navigateToFriendsScreen,
                navigateToChat = navigateToChatScreen
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
                    // Here you could display an error message or some error UI
                    Text("Error loading profile")
                }
                is Response.Success -> {
                    ProfileContent(
                        padding = padding,
                        userProfile = viewModel.userProfile,
                        onEdit = {
                            onEditProfile()
                        }
                    )
                }
            }
        },
        scaffoldState = scaffoldState
    )

    RevokeAccess(
        scaffoldState = scaffoldState,
        coroutineScope = coroutineScope,
        signOut = {
            viewModel.signOut()
        }
    )

    val currentUserUid = viewModel.getCurrentUserUid()
    LaunchedEffect(currentUserUid) {
        currentUserUid?.let { viewModel.loadUserProfile(it) }
    }
}

