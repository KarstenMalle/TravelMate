package com.example.travelmate.ui.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.travelmate.domain.model.UserProfile

@Composable
fun ProfileContent(
    padding: PaddingValues,
    userProfile: UserProfile?,
    onEdit: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding).padding(top = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Welcome to TravelMate!",
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Check if the profile image URL is not null
            if (!userProfile?.photoUrl.isNullOrEmpty()) {
                val painter = rememberImagePainter(data = userProfile?.photoUrl)

                Image(
                    painter = painter,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text("Name: ${userProfile?.fullName ?: "N/A"}")
            Text("Email: ${userProfile?.email ?: "N/A"}")

            Spacer(modifier = Modifier.weight(1f)) // Spacer to push the button to the bottom

            Button(
                onClick = { onEdit() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Edit Profile")
            }
        }
    }
}
