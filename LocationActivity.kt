package com.example.naturaldisaster

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class LocationActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var tvPlace: TextView
    private lateinit var btnLocation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        // OSM Configuration
        Configuration.getInstance().load(
            applicationContext,
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        mapView = findViewById(R.id.mapView)
        tvPlace = findViewById(R.id.tvPlace)
        btnLocation = findViewById(R.id.btnLocation)

        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(15.0)

        btnLocation.setOnClickListener { getCurrentLocation() }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
            if (loc != null) {
                val lat = loc.latitude
                val lng = loc.longitude

                getPlaceFromOSM(lat, lng) { place ->
                    tvPlace.text = "$place\nLat: $lat, Lng: $lng"
                    showMarkerOnMap(lat, lng, place)
                }

            } else {
                Toast.makeText(this, "Location unavailable", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showMarkerOnMap(lat: Double, lng: Double, title: String) {
        val geoPoint = GeoPoint(lat, lng)
        mapView.controller.setCenter(geoPoint)

        val marker = Marker(mapView).apply {
            position = geoPoint
            this.title = title
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }

        mapView.overlays.clear()
        mapView.overlays.add(marker)
        mapView.invalidate()
    }

    private fun getPlaceFromOSM(lat: Double, lng: Double, callback: (String) -> Unit) {
        val client = OkHttpClient()
        val url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=$lat&lon=$lng"

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "NaturalDisasterApp/1.0")
                    .build()

                val response = client.newCall(request).execute()
                val body = response.body?.string()

                val placeName = if (body != null) {
                    val json = JSONObject(body)
                    val address = json.optJSONObject("address")
                    address?.optString("city")
                        ?: address?.optString("town")
                        ?: address?.optString("village")
                        ?: address?.optString("suburb")
                        ?: "Unknown Location"
                } else "Unknown Location"

                withContext(Dispatchers.Main) { callback(placeName) }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) { callback("Unknown Location") }
            }
        }
    }
}
