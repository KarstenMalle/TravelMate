package com.example.travelmate.ui.map

import BottomBar
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelmate.R
import com.example.travelmate.navigation.Screen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.MarkerOptions

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapsScreen(
    viewModel: MapsViewModel = hiltViewModel(),
    navigateToProfileScreen: () -> Unit,
    navigateToFriendsScreen: () -> Unit,
    navigateToChatScreen: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val scaffoldState = rememberScaffoldState()
    val location by viewModel.centerOnLocation.observeAsState(initial = null)
    val markers by viewModel.markers.observeAsState(initial = mutableListOf())
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            mapView.getMapAsync { googleMap ->
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = true
                viewModel.centerOnUserLocation()
            }
        } else {
            // Show an explanation to the user if necessary
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Map") },
                actions = {
                    IconButton(onClick = {
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        } else {
                            mapView.getMapAsync { googleMap ->
                                googleMap.isMyLocationEnabled = true
                                googleMap.uiSettings.isMyLocationButtonEnabled = true
                                viewModel.centerOnUserLocation()
                            }
                        }
                    }) {
                        Icon(Icons.Filled.LocationOn, contentDescription = "Center Location")
                    }
                    IconButton(onClick = { viewModel.removeMarkers() }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Remove Marker")
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                currentRoute = Screen.ProfileScreen.route,
                navigateToProfile = navigateToProfileScreen,  // No action needed when already on profile screen
                navigateToMap = {},
                navigateToFriends = navigateToFriendsScreen,
                navigateToChat = navigateToChatScreen
            )
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView({ mapView }) { mapView ->
                    mapView.getMapAsync { googleMap ->

                        // Listener for user clicks on the map
                        googleMap.setOnMapClickListener { latLng ->
                            // Use viewModel to add marker to LiveData
                            viewModel.addMarker(latLng)
                        }

                        // Observe markers LiveData and update the map
                        viewModel.markers.observe(lifecycleOwner) { markers ->
                            googleMap.clear()
                            markers.forEach { latLng ->
                                googleMap.addMarker(MarkerOptions().position(latLng))
                            }
                        }

                        // Observe centerOnLocation LiveData and update the map
                        viewModel.centerOnLocation                        .observe(lifecycleOwner) { location ->
                            location?.let { latLng ->
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                            }
                        }

                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            googleMap.isMyLocationEnabled = true
                            googleMap.uiSettings.isMyLocationButtonEnabled = true
                        }
                    }
                }
            }
        },
        scaffoldState = scaffoldState
    )
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
            onCreate(null)
        }
    }

    DisposableEffect(context) {
        mapView.onStart()
        mapView.onResume()

        onDispose {
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
        }
    }

    return mapView
}

