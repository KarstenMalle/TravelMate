package com.example.travelmate.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelmate.domain.model.ChatMessage
import com.example.travelmate.ui.friends.components.RemoteImage

@Composable
fun ChatScreen(
    friendId: String,
    navigateBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val messages = viewModel.messages
    val friendProfile = viewModel.friendProfile
    val textState = remember { mutableStateOf(TextFieldValue()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(Modifier.padding(8.dp)) {
                        friendProfile?.photoUrl?.let {
                            RemoteImage(url = it)
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(friendProfile?.fullName ?: "")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    reverseLayout = true,
                    contentPadding = padding
                ) {
                    items(messages.sortedByDescending { it.timestamp }) { message -> // Sort messages by timestamp in descending order
                        MessageBubble(message = message, isOwnMessage = message.uid_from == viewModel.getCurrentUserUid())
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        value = textState.value,
                        onValueChange = { textState.value = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    Button(
                        onClick = {
                            viewModel.sendMessage(friendId, textState.value.text, friendProfile?.fullName ?: "", navigateBack = {
                                textState.value = TextFieldValue() // Clear text field after sending message
                            })
                        },
                        enabled = textState.value.text.isNotBlank() // Only enable button if there's text to send
                    ) {
                        Icon(Icons.Filled.Send, contentDescription = "Send")
                    }
                }
            }
        }
    )

    LaunchedEffect(friendId) {
        viewModel.getFriendProfile(friendId)
        viewModel.loadMessages(friendId)
    }
}

@Composable
fun MessageBubble(message: ChatMessage, isOwnMessage: Boolean) {
    val alignment = if (isOwnMessage) Alignment.End else Alignment.Start
    val backgroundColor = if (isOwnMessage) Color(0xFF6200EE) else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = if(isOwnMessage) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Text(
            text = message.message,
            color = if (isOwnMessage) Color.White else Color.Black,
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                )
        )
    }
}
