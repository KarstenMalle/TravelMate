package com.example.travelmate.ui.map


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class MapsViewModel : ViewModel() {

    private val _markers = MutableLiveData<MutableList<Marker>>(mutableListOf())
    val markers: LiveData<MutableList<Marker>> get() = _markers

    private val _centerOnLocation = MutableLiveData<LatLng?>()
    val centerOnLocation: LiveData<LatLng?> get() = _centerOnLocation

    fun addMarker(marker: Marker) {
        _markers.value?.add(marker)
    }

    fun removeMarkers() {
        _markers.value?.clear()
    }

    fun centerOnUserLocation(location: LatLng) {
        _centerOnLocation.value = location
    }

    fun locationCentered() {
        _centerOnLocation.value = null
    }
}
