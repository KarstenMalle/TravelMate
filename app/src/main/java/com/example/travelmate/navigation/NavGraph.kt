package com.example.travelmate.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.travelmate.navigation.Screen.*
import com.example.travelmate.ui.chat.ChatScreen
import com.example.travelmate.ui.chat.views.ChatOverviewScreen
import com.example.travelmate.ui.chat.views.NewChatScreen
import com.example.travelmate.ui.edit_profile.EditProfileScreen
import com.example.travelmate.ui.forgot_pw.ForgotPasswordScreen
import com.example.travelmate.ui.friends.FriendsScreen
import com.example.travelmate.ui.friends.views.AddFriendsScreen
import com.example.travelmate.ui.login.LoginScreen
import com.example.travelmate.ui.profile.ProfileScreen
import com.example.travelmate.ui.sign_up.SignUpScreen
import com.example.travelmate.ui.verify_email.VerifyEmailScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.example.travelmate.ui.map.MapsScreen

@Composable
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
fun NavGraph(
    navController: NavHostController,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = SignInScreen.route,
        enterTransition = {EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(
            route = SignInScreen.route
        ) {
            LoginScreen(
                navigateToForgotPasswordScreen = {
                    navController.navigate(ForgotPasswordScreen.route)
                },
                navigateToSignUpScreen = {
                    navController.navigate(SignUpScreen.route)
                }
            )
        }
        composable(
            route = ForgotPasswordScreen.route
        ) {
            ForgotPasswordScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = SignUpScreen.route
        ) {
            SignUpScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = VerifyEmailScreen.route
        ) {
            VerifyEmailScreen(
                navigateToProfileScreen = {
                    navController.navigate(ProfileScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = ProfileScreen.route
        ) {
            ProfileScreen(
                viewModel = hiltViewModel(),
                onEditProfile = {
                    navController.navigate(EditProfileScreen.route)
                },
                navigateToFriendsScreen = {
                    navController.navigate(FriendsScreen.route)
                },
                navigateToChatScreen = {
                    navController.navigate(ChatOverviewScreen.route)
                },
                navigateToMapScreen = {
                    navController.navigate(MapsScreen.route)
                }
            )
        }
        composable(
            route = EditProfileScreen.route
        ) {
            EditProfileScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = FriendsScreen.route
        ) {
            FriendsScreen(
                onAddFriend = {
                    navController.navigate(AddFriendsScreen.route)
                },
                onStartChat = { route ->
                    navController.navigate(route)
                },
                navigateToProfileScreen = {
                    navController.navigate(ProfileScreen.route)
                },
                navigateToChatScreen = {
                    navController.navigate(ChatOverviewScreen.route)
                },
                navigateToMapScreen = {
                    navController.navigate(MapsScreen.route)
                }
            )
        }
        composable(
            route = AddFriendsScreen.route
        ) {
            AddFriendsScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = ChatOverviewScreen.route
        ) {
            ChatOverviewScreen(
                onNewChat = {
                    navController.navigate(NewChatScreen.route)
                },
                onOpenChat = { chatId ->
                    navController.navigate("${ChatScreen.route}/$chatId")
                },
                navigateToProfileScreen = {
                    navController.navigate(ProfileScreen.route)
                },
                navigateToFriendsScreen = {
                    navController.navigate(FriendsScreen.route)
                },
                navigateToMapScreen = {
                    navController.navigate(MapsScreen.route)
                }
            )
        }
        composable(
            route = NewChatScreen.route
        ) {
            NewChatScreen(
                navigateToChat = { route ->
                    navController.navigate(route)
                },
                navigateBack = {
                    navController.popBackStack()
                }

            )
        }
        composable(
            route = "${ChatScreen.route}/{friendId}"
        ) { backStackEntry ->
            val friendId = backStackEntry.arguments?.getString("friendId")
            ChatScreen(
                friendId = friendId ?: "",
                navigateBack = {
                    navController.popBackStack()
                },
                viewModel = hiltViewModel()
            )
        }
        composable(
            route = MapsScreen.route
        ) {
            MapsScreen(
                viewModel = hiltViewModel(),
                navigateToProfileScreen = {
                    navController.navigate(ProfileScreen.route)
                },
                navigateToFriendsScreen = {
                    navController.navigate(FriendsScreen.route)
                },
                navigateToChatScreen = {
                    navController.navigate(ChatOverviewScreen.route)
                },
            )

        }
    }
}