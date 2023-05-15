package com.example.travelmate.ui.chat.views

import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelmate.navigation.Screen


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatOverviewScreen(
    viewModel: ChatOverviewViewModel = hiltViewModel(),
    onNewChat: () -> Unit,
    onOpenChat: (chatId: String) -> Unit,
    navigateToProfileScreen: () -> Unit,
    navigateToFriendsScreen: () -> Unit,
    navigateToMapScreen: () -> Unit
) {
    val chats by viewModel.chats.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Chats") },
                actions = {
                    IconButton(onClick = onNewChat) {
                        Icon(Icons.Default.Add, contentDescription = "Start new chat")
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                currentRoute = Screen.ProfileScreen.route,
                navigateToProfile = navigateToProfileScreen,  // No action needed when already on profile screen
                navigateToMap = navigateToMapScreen,
                navigateToFriends = navigateToFriendsScreen,
                navigateToChat = {},
            )
        },
        content = {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn {
                    items(chats) { chat ->
                        Row {
                            // Assuming there's a "lastMessage" field in your Chat data class
                            Text(
                                text = chat.lastMessage,
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.onSurface,
                                modifier = Modifier.clickable { onOpenChat(chat.id) }
                            )
                        }
                    }
                }
            }
        }
    )
}
