package com.adista.finalproject.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adista.finalproject.AddressHelper
import com.adista.finalproject.R
import com.adista.finalproject.databinding.ActivityMapsBinding
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.checkLocationPermission
import com.crocodic.core.helper.LocationHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapsActivity : NoViewModelActivity<ActivityMapsBinding>(R.layout.activity_maps){

    @Inject
    lateinit var adrHelper : AddressHelper

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.mapView.onCreate(savedInstanceState)

        checkLocationPermission {
            listenLocationChange()
        }

        binding.mapView.getMapAsync{ googleMap ->

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@getMapAsync
            }
            googleMap.isMyLocationEnabled = true

            val area = googleMap.addCircle(CircleOptions()
                .center(LatLng(-7.4587597,109.2307686))
                .radius(1_000.0)
                .strokeColor(Color.parseColor("#FFC80000"))
                .fillColor(Color.parseColor("#25C80000")))
        }
    }

    private fun isInsideLocation(area: LatLng, position: LatLng) : Boolean {
        return LocationHelper.distance(area, position) < 1
    }

    @SuppressLint("SetTextI18n")
    override fun retrieveLocationChange(location: Location) {
        super.retrieveLocationChange(location)
        Log.d("deviceLocation", "latitude: ${location.latitude}, longitude: ${location.longitude}")

        binding.mapView.getMapAsync {
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 13f))

            val isInside = isInsideLocation(LatLng(-7.4587597,109.2307686), LatLng(location.latitude, location.longitude))

            val status = if(isInside) "dalam" else "luar"

            binding.tvStatus.text = "Kamu berada di $status area."
        }
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
