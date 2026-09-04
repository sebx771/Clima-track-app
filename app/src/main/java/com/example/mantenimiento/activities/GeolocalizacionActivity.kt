package com.example.mantenimiento.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mantenimiento.R
import com.example.mantenimiento.databinding.ActivityGeolocalizacionBinding
import com.example.mantenimiento.utils.LocationUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.*

class GeolocalizacionActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityGeolocalizacionBinding
    private var googleMap: GoogleMap? = null
    private val locationPermissionRequestCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeolocalizacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        
        // Inicialización de MapView de Google
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        binding.btnActualizarUbicacion.setOnClickListener {
            checkPermissionsAndGetLocation()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarGeo)
        binding.toolbarGeo.setNavigationOnClickListener { finish() }
    }

    override fun onMapReady(map: GoogleMap) {
        if (isFinishing || isDestroyed) return
        
        googleMap = map
        googleMap?.apply {
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMyLocationButtonEnabled = false
        }
        
        // Pequeño delay para asegurar fluidez en la carga inicial
        binding.root.postDelayed({
            checkPermissionsAndGetLocation()
        }, 500)
    }

    private fun checkPermissionsAndGetLocation() {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
            (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                locationPermissionRequestCode
            )
            return
        }
        
        obtenerUbicacionActual()
    }

    private fun obtenerUbicacionActual() {
        binding.tvStatusUbicacion.visibility = View.GONE
        binding.tvErrorUbicacion.visibility = View.GONE
        
        LocationUtils.getCurrentLocation(this) { location ->
            runOnUiThread {
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    actualizarUI(latLng)
                    binding.tvStatusUbicacion.visibility = View.VISIBLE
                } else {
                    binding.tvErrorUbicacion.visibility = View.VISIBLE
                    Toast.makeText(this, getString(R.string.status_ubicacion_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun actualizarUI(latLng: LatLng) {
        if (isFinishing || isDestroyed) return

        googleMap?.let { map ->
            try {
                map.clear()
                map.addMarker(MarkerOptions().position(latLng).title(getString(R.string.label_map_title)))
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.tvLatitud.text = String.format(Locale.US, "%.6f", latLng.latitude)
        binding.tvLongitud.text = String.format(Locale.US, "%.6f", latLng.longitude)

        val fechaHora = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        binding.tvFechaHora.text = fechaHora

        obtenerDireccion(latLng)
    }

    private fun obtenerDireccion(latLng: LatLng) {
        Thread {
            try {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                
                runOnUiThread {
                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        val addressString = address.getAddressLine(0) ?: getString(R.string.label_dir_no_encontrada)
                        binding.tvDireccion.text = addressString
                    } else {
                        binding.tvDireccion.text = getString(R.string.label_dir_no_disponible)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    binding.tvDireccion.text = getString(R.string.label_dir_error)
                }
            }
        }.start()
    }

    // Métodos obligatorios del ciclo de vida para MapView
    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        binding.mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionActual()
            } else {
                Toast.makeText(this, getString(R.string.msg_permiso_denegado), Toast.LENGTH_LONG).show()
                binding.tvErrorUbicacion.visibility = View.VISIBLE
            }
        }
    }
}
