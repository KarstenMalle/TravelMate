package com.example.travelmate.ui.chat.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.travelmate.navigation.Screen
import com.example.travelmate.ui.components.BottomBar


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
    val errorMessage by viewModel.errorMessage.observeAsState(null)

    LaunchedEffect(key1 = viewModel) {
        viewModel.loadChats()
    }

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
                navigateToProfile = navigateToProfileScreen,
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
            } else if (errorMessage != null) {
                Text(text = "Error: $errorMessage")
            } else {
                LazyColumn {
                    items(chats) { chat ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenChat(chat.id) }
                                .padding(16.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(chat.photoUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = chat.fullName,
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.colors.onSurface,
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = chat.lastMessage,
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.onSurface,
                                )
                            }

                            Text(
                                text = viewModel.getFormattedTime(chat.lastMessageTimestamp),
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface,
                                textAlign = TextAlign.End,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }

            }
        }
    )
}
