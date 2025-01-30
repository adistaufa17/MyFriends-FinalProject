package com.adista.finalproject.activity

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adista.finalproject.R
import com.adista.finalproject.databinding.ActivityMapsBinding
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.checkLocationPermission
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : NoViewModelActivity<ActivityMapsBinding>(R.layout.activity_maps), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = true

        val defaultLocation = LatLng(-6.200000, 106.816666)
        googleMap.addMarker(
            MarkerOptions()
                .position(defaultLocation)
                .title("Jakarta")
                .snippet("Ibu Kota Indonesia")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f))

        checkLocationPermission {
            listenLocationChange()
        }
    }

    override fun retrieveLocationChange(location: Location) {
        super.retrieveLocationChange(location)
        Log.d("deviceLocation", "latitude: ${location.latitude}, longitude: ${location.longitude}")

        val userLocation = LatLng(location.latitude, location.longitude)
        googleMap.addMarker(
            MarkerOptions()
                .position(userLocation)
                .title("Lokasi Anda")
        )
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}
