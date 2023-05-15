package com.example.travelmate.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.travelmate.R
import com.example.travelmate.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


@Suppress("DEPRECATION")
class MapsScreen : AppCompatActivity(), OnMapReadyCallback {
    private val mapsViewModel: MapsViewModel by viewModels()
    private lateinit var mMap: GoogleMap
    private var centerOnLocation = false
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private var locationEnabled = false
    private var currentMarker: Marker? = null

    private val markerList = mutableListOf<Marker>()
    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    private val locationCallback = MyLocationCallback()
    inner class MyLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                // Add a marker at the user's current location
                mapsViewModel.centerOnUserLocation(LatLng(location.latitude, location.longitude))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.locationButton.setOnClickListener {
            centerOnLocation = !centerOnLocation
        }

        binding.removeMarkerButton.setOnClickListener {
            removeMarker()
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mapsViewModel.centerOnLocation.observe(this, Observer { location ->
            location?.let {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 14f))
                mapsViewModel.locationCentered()
            }
        })

        // Set listener for marker clicks
        mMap.setOnMarkerClickListener { marker ->
            if (marker.tag == "my_marker") {
                showMarkerOptionsDialog(marker)
                false // Change this from true to false
            } else {
                // Show a toast with the marker's title
                Toast.makeText(this, marker.title, Toast.LENGTH_SHORT).show()
                true
            }
        }
        mapsViewModel.markers.observe(this, Observer { markers ->
            markers.forEach { it.remove() }
        })

        // Disable map toolbar and compass
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isCompassEnabled = false

        // Enable location button
        mMap.uiSettings.isMyLocationButtonEnabled = true

        // Set listener for location button clicks
        mMap.setOnMyLocationButtonClickListener {
            locationEnabled = true
            updateMapLocation()
            true
        }

        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        // Enable location layer
        mMap.isMyLocationEnabled = true

        // Set long click listener to add a marker
        mMap.setOnMapLongClickListener { latLng ->
            val markerOptions = MarkerOptions().position(latLng).title("New Marker")
            val marker = mMap.addMarker(markerOptions)
            marker?.let {
                mapsViewModel.addMarker(it)
                // Set tag for the newly added marker
                it.tag = "my_marker"
            }
        }

        // Set click listener to remove all markers
        mMap.setOnMapClickListener { _ ->
            mapsViewModel.removeMarkers()
        }

        // Get the user's current location
        locationProviderClient.lastLocation.addOnSuccessListener { location ->
            // Add a marker at the user's current location
            val userLocation = LatLng(location.latitude, location.longitude)
            mMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14f))
        }
    }
    private fun updateMapLocation() {
        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permission if it has not been granted
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            // Start location updates
            locationProviderClient.requestLocationUpdates(
                LocationRequest.create(),
                locationCallback,
                null
            )
        }
        if (centerOnLocation) {
            locationProviderClient.lastLocation.addOnSuccessListener { location ->
                // Add a marker at the user's current location
                val userLocation = LatLng(location.latitude, location.longitude)
                currentMarker?.remove()
                currentMarker = mMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14f))
            }
        }
    }

    private fun removeMarker() {
        // Check if map is ready
        if (::mMap.isInitialized) {
            // Remove the last added marker
            if (markerList.isNotEmpty()) {
                val lastMarker = markerList.last()
                lastMarker.remove()
                markerList.removeLast()
            } else {
                Toast.makeText(this, "No markers to remove", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Show a dialog with options for a clicked marker
    private fun showMarkerOptionsDialog(clickedMarker: Marker) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("What do you want to do with this marker?")
            .setCancelable(true)
            .setPositiveButton("Share") { dialog, _ ->
                // Share marker
                dialog.dismiss()
            }
            .setNegativeButton("Delete") { dialog, _ ->
                // Delete marker
                clickedMarker.remove()
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Marker Options")
        alert.show()
    }
}
