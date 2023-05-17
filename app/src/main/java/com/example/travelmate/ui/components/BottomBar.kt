package com.example.travelmate.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    Profile("profile", Icons.Default.Person, "Profile"),
    Map("map", Icons.Default.Map, "Map"),
    Friends("friends", Icons.Default.People, "Friends"),
    Chat("chat", Icons.Default.Chat, "Chat")
}

@Composable
fun BottomBar(
    currentRoute: String,
    navigateToProfile: () -> Unit,
    navigateToMap: () -> Unit,
    navigateToFriends: () -> Unit,
    navigateToChat: () -> Unit
) {
    BottomNavigation {
        BottomNavItem.values().forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.name) },
                selected = currentRoute == item.route,
                label = { Text(text = item.title) },
                alwaysShowLabel = true,
                selectedContentColor = Color.White,
                onClick = {
                    when (item) {
                        BottomNavItem.Profile -> navigateToProfile()
                        BottomNavItem.Map -> navigateToMap()
                        BottomNavItem.Friends -> navigateToFriends()
                        BottomNavItem.Chat -> navigateToChat()
                    }
                },
            )
        }
    }
}
