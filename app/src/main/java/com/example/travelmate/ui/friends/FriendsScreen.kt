package com.example.travelmate.ui.friends

import BottomBar
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
import com.example.travelmate.ui.components.TopBar
import com.example.travelmate.ui.friends.components.FriendsContent

@Composable
fun FriendsScreen(
    viewModel: FriendsViewModel = hiltViewModel(),
    onAddFriend: () -> Unit,
    onStartChat: (String) -> Unit,
    navigateToProfileScreen: () -> Unit,
    navigateToChatScreen: () -> Unit,
    navigateToMapScreen: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val friendsLoading = viewModel.friendsLoading

    Scaffold(
        topBar = {
            TopBar(
                title = "Friends",
                signOut = {
                    viewModel.signOut()
                }
            )
        },
        bottomBar = {
            BottomBar(
                currentRoute = Screen.FriendsScreen.route,
                navigateToProfile = navigateToProfileScreen,
                navigateToMap = navigateToMapScreen,
                navigateToFriends = {},
                navigateToChat = navigateToChatScreen
            )
        },
        content = { padding ->
            when (friendsLoading) {
                is Response.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is Response.Failure -> {
                    // Here you could display an error message or some error UI
                    Text("Error loading friends")
                }
                is Response.Success -> {
                    FriendsContent(
                        padding = padding,
                        friends = viewModel.friends,
                        onAddFriend = {
                            onAddFriend()
                        },
                        onStartChat = { friendUid ->
                            onStartChat(friendUid)
                        }
                    )
                }
            }
        },
        scaffoldState = scaffoldState
    )

    LaunchedEffect(Unit) {
        viewModel.loadFriends(10)
    }
}
