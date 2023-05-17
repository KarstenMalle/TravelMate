package com.example.travelmate.navigation

sealed class Screen(val route: String) {
    object SignInScreen: Screen("Login")
    object ForgotPasswordScreen: Screen("Forgot password")
    object SignUpScreen: Screen("Sign up")
    object VerifyEmailScreen: Screen("Verify email")
    object ProfileScreen: Screen("Profile")
    object EditProfileScreen: Screen("Edit profile")
    object MapsScreen: Screen("Map")
    object FriendsScreen: Screen("Friends")
    object AddFriendsScreen: Screen("Add Friends")
    object ChatScreen: Screen("Chat")
    object ChatOverviewScreen: Screen("Overview chat")
    object NewChatScreen: Screen("New chat")
}