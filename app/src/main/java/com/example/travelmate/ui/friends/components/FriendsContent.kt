package com.example.travelmate.ui.friends.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.travelmate.domain.model.UserProfile
import com.example.travelmate.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


@Composable
fun FriendsContent(
    padding: PaddingValues,
    friends: List<UserProfile>,
    onAddFriend: () -> Unit,
    onStartChat: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(friends) { friend ->
                Row {
                    friend.photoUrl?.let { RemoteImage(it) } // Display friend's profile picture
                    Spacer(Modifier.size(20.dp))
                    Text(
                        text = friend.fullName,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.clickable { onStartChat("${Screen.ChatScreen.route}/${friend.uid}") }
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Button(onClick = onAddFriend) {
            Text("Add Friend")
        }
    }
}

suspend fun loadPicture(url: String): ImageBitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()

            val inputStream: InputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)
            bitmap.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@Composable
fun RemoteImage(url: String) {
    var image by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(url) {
        image = loadPicture(url)
    }

    image?.let { img ->
        Image(
            bitmap = img,
            contentDescription = null,
            modifier = Modifier.size(40.dp) // Adjust size as needed
        )
    }
}

