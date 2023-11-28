package com.dicoding.mystoryapp.view.maps

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.data.api.ListStoryItem
import com.dicoding.mystoryapp.databinding.ActivityMapsBinding
import com.dicoding.mystoryapp.view.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var allLocations: ArrayList<ListStoryItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getAllLocations()
        setMapStyle()

    }

    private fun getAllLocations() {
        allLocations = ArrayList()
        viewModel.getStoriesWithLocation().observe(this) {response ->
            allLocations.clear()
            allLocations.addAll(response.listStory)
            val latLngBounds = LatLngBounds.Builder()

            allLocations.forEach { location ->
                val latLng = LatLng(location.lat.toString().toDouble(), location.lon.toString().toDouble())
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(location.name)
                        .snippet(location.description)
                )
                latLngBounds.include(latLng)
            }

            val bounds: LatLngBounds = latLngBounds.build()

            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    400
                )
            )

        }
    }
    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }
    companion object {
        const val TAG = "MapsActivity"
    }
}