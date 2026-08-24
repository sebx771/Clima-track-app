package com.example.mantenimiento.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.databinding.ActivityDashboardBinding
import com.example.mantenimiento.repository.OrdenRepository

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var ordenRepository: OrdenRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ordenRepository = OrdenRepository(this)

        cargarDatosUsuario()
        cargarContadores()

        binding.btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

        // Evento para conectar con el Módulo 3 más adelante
        binding.btnVerOrdenes.setOnClickListener {
            startActivity(Intent(this, OrdenesActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Actualiza contadores si el técnico regresa a esta pantalla tras modificar órdenes
        cargarContadores()
    }

    private fun cargarDatosUsuario() {
        val prefs = getSharedPreferences("ClimaTrackPrefs", Context.MODE_PRIVATE)
        val nombre = prefs.getString("nombreTecnico", "Técnico")
        binding.tvSaludo.text = "Hola, $nombre"
    }

    private fun cargarContadores() {
        binding.tvCountPendientes.text = ordenRepository.contarOrdenesPorEstado("PENDIENTE").toString()
        binding.tvCountEnProceso.text = ordenRepository.contarOrdenesPorEstado("EN PROCESO").toString()
        binding.tvCountFinalizadas.text = ordenRepository.contarOrdenesPorEstado("FINALIZADA").toString()
    }

    private fun cerrarSesion() {
        val prefs = getSharedPreferences("ClimaTrackPrefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}