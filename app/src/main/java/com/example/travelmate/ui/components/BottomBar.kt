import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItem(val route: String, val icon: ImageVector) {
    Profile("profile", Icons.Default.AccountCircle),
    Map("map", Icons.Default.Map),
    Friends("friends", Icons.Default.People),
    Chat("chat", Icons.Default.Chat)
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
                onClick = {
                    when (item) {
                        BottomNavItem.Profile -> navigateToProfile()
                        BottomNavItem.Map -> navigateToMap()
                        BottomNavItem.Friends -> navigateToFriends()
                        BottomNavItem.Chat -> navigateToChat()
                    }
                },
                alwaysShowLabel = false
            )
        }
    }
}
