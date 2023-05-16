package com.example.travelmate.ui.map

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : ViewModel() {

    private val _markers = MutableLiveData<MutableList<LatLng>>(mutableListOf())
    val markers: LiveData<MutableList<LatLng>> get() = _markers

    private val _centerOnLocation = MutableLiveData<LatLng?>()
    val centerOnLocation: LiveData<LatLng?> get() = _centerOnLocation

    fun addMarker(latLng: LatLng) {
        val list = _markers.value ?: mutableListOf()
        list.add(latLng)
        _markers.value = list
    }

    fun removeMarker(latLng: LatLng) {
        _markers.value?.remove(latLng)
    }

    fun removeMarkers() {
        _markers.value?.clear()
    }


    @SuppressLint("MissingPermission")
    fun centerOnUserLocation() {
        // Here you need to implement logic to get user's current location
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                _centerOnLocation.value = LatLng(location.latitude, location.longitude)
            }
        }
    }
}