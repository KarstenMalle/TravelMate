package com.example.travelmate.ui.friends.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
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
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(friends) { friend ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onStartChat("${Screen.ChatScreen.route}/${friend.uid}") }
                        .padding(16.dp)
                ) {
                    friend.photoUrl?.let {
                        RemoteImage(it)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = friend.fullName,
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.onSurface,
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = constructInterestsString(friend.interests, 30),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface,
                            maxLines = 1,
                        )
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Button(onClick = onAddFriend) {
                Text("Add Friend")
            }
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
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}


fun constructInterestsString(interests: List<String>, maxLength: Int): String {
    val result = StringBuilder()
    var length = 0
    for (interest in interests) {
        val words = interest.split(" ")
        for (word in words) {
            if (length + word.length + 2 > maxLength) {
                return "${result}..."
            }
            if (result.isNotEmpty()) {
                result.append(" ")
                length++
            }
            result.append(word)
            length += word.length
        }
        result.append(",")
        length++
    }
    if (length > 0 && result[length - 1] == ',') {
        result.deleteCharAt(length - 1)
    }
    return result.toString()
}







