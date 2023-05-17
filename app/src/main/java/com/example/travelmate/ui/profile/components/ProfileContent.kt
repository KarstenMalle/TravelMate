package com.example.travelmate.ui.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.travelmate.domain.model.UserProfile

@Composable
fun ProfileContent(
    padding: PaddingValues,
    userProfile: UserProfile?,
    onEdit: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Welcome to TravelMate!",
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (!userProfile?.photoUrl.isNullOrEmpty()) {
                val painter = rememberAsyncImagePainter(model = userProfile?.photoUrl)

                Image(
                    painter = painter,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Column {
                    ProfileDetail(title = "Name", value = userProfile?.fullName ?: "N/A")
                    Divider(color = Color.Black)
                    ProfileDetail(title = "Age", value = userProfile?.age?.toString() ?: "N/A")
                    Divider(color = Color.Black)
                    ProfileDetail(title = "Country", value = userProfile?.homeCountry ?: "N/A")
                    Divider(color = Color.Black)
                    ProfileDetail(title = "Bio", value = userProfile?.bio ?: "N/A", maxLines = 2)
                    Divider(color = Color.Black)
                    ProfileDetail(
                        title = "Interests",
                        value = userProfile?.interests?.joinToString() ?: "N/A",
                        maxLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { onEdit() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Edit Profile")
            }
        }
    }
}

@Composable
fun ProfileDetail(title: String, value: String, maxLines: Int = 1) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            maxLines = maxLines
        )
    }
}