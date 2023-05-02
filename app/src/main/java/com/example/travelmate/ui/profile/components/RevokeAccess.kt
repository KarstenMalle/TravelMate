package com.example.travelmate.ui.profile.components

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.example.travelmate.ui.components.ProgressBar
import com.example.travelmate.core.Utils.Companion.print
import com.example.travelmate.core.Utils.Companion.showMessage
import com.example.travelmate.domain.model.Response.*
import com.example.travelmate.ui.profile.ProfileViewModel

@Composable
fun RevokeAccess(
    viewModel: ProfileViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    signOut: () -> Unit,
) {
    val context = LocalContext.current

    fun showRevokeAccessMessage() = coroutineScope.launch {
        val result = scaffoldState.snackbarHostState.showSnackbar(
            message = "Delete account",
            actionLabel = "Logout"
        )
        if (result == SnackbarResult.ActionPerformed) {
            signOut()
        }
    }

    when(val revokeAccessResponse = viewModel.revokeAccessResponse) {
        is Loading -> ProgressBar()
        is Success -> {
            val isAccessRevoked = revokeAccessResponse.data
            LaunchedEffect(isAccessRevoked) {
                if (isAccessRevoked) {
                    showMessage(context, "Your account has been deleted")
                }
            }
        }
        is Failure -> revokeAccessResponse.apply {
            LaunchedEffect(e) {
                print(e)
                if (e.message == "This operation is sensitive and requires recent authentication. Login again before retrying this request.") {
                    showRevokeAccessMessage()
                }
            }
        }
    }
}