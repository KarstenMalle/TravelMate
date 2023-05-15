package com.example.travelmate.ui.chat.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelmate.domain.model.Response
import com.example.travelmate.ui.chat.ChatViewModel
import com.example.travelmate.ui.friends.components.RemoteImage

@Composable
fun NewChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    navigateToChat: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val friends = viewModel.friends
    val friendsLoading = viewModel.friendsLoading
    val scaffoldState = rememberScaffoldState()

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("New chat") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
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
                    LazyColumn(contentPadding = padding) { // Apply padding here
                        items(friends) { friend ->
                            Text(
                                text = friend.fullName,
                                modifier = Modifier.clickable { navigateToChat(friend.uid) }
                            )
                            friend.photoUrl?.let { RemoteImage(it) } // Display friend's profile picture
                        }
                    }
                }
            }
        },
        scaffoldState = scaffoldState
    )

    LaunchedEffect(Unit) {
        viewModel.loadFriends(10)
    }
}

