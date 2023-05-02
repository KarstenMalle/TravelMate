package com.example.travelmate.navigation

sealed class Screen(val route: String) {
    object SignInScreen: Screen("Login")
    object ForgotPasswordScreen: Screen("Forgot password")
    object SignUpScreen: Screen("Sign up")
    object VerifyEmailScreen: Screen("Verify email")
    object ProfileScreen: Screen("Profile")
}