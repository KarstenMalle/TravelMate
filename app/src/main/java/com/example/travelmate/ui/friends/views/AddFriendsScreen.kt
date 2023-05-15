package com.example.travelmate.ui.friends.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelmate.ui.friends.FriendsViewModel

@Composable
fun AddFriendsScreen(
    viewModel: FriendsViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    // var searchResults = viewModel.searchFriends(searchQuery) // add this function in your ViewModel

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Friends") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.searchFriends(it)
                    },
                    label = { Text("Search Friends") },
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(modifier = Modifier.height(16.dp))

                viewModel.searchResults.forEach { friend ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = friend.fullName,
                            modifier = Modifier.weight(1f)
                        )
                        Button(onClick = {
                            viewModel.addFriend(friend.uid, navigateBack)
                        })
                        {
                            Text("Add")
                        }
                    }
                }

            }
        }
    )
}
