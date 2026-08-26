package com.example.mantenimiento.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mantenimiento.R
import com.example.mantenimiento.activities.LoginActivity
import com.example.mantenimiento.repository.OrdenRepository
import com.google.android.material.button.MaterialButton

import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardFragment : Fragment() {

    private lateinit var ordenRepository: OrdenRepository
    private lateinit var tvSaludo: TextView
    private lateinit var tvCountPendientes: TextView
    private lateinit var tvCountEnProceso: TextView
    private lateinit var tvCountFinalizadas: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        ordenRepository = OrdenRepository(requireContext())
        
        tvSaludo = view.findViewById(R.id.tvSaludo)
        tvCountPendientes = view.findViewById(R.id.tvCountPendientes)
        tvCountEnProceso = view.findViewById(R.id.tvCountEnProceso)
        tvCountFinalizadas = view.findViewById(R.id.tvCountFinalizadas)

        cargarDatosUsuario()
        cargarContadores()

        // Navegación entre pestañas
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)

        view.findViewById<MaterialButton>(R.id.btnVerOrdenes).setOnClickListener {
            bottomNav.selectedItemId = R.id.nav_ordenes
        }

        view.findViewById<MaterialButton>(R.id.btnVerEquipos).setOnClickListener {
            bottomNav.selectedItemId = R.id.nav_equipos
        }

        view.findViewById<MaterialButton>(R.id.btnGridHistorial).setOnClickListener {
            bottomNav.selectedItemId = R.id.nav_historial
        }

        view.findViewById<MaterialButton>(R.id.btnCerrarSesion).setOnClickListener {
            cerrarSesion()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        cargarContadores()
    }

    private fun cargarDatosUsuario() {
        val prefs = requireContext().getSharedPreferences("ClimaTrackPrefs", Context.MODE_PRIVATE)
        val nombre = prefs.getString("nombreTecnico", "Técnico")
        tvSaludo.text = "Hola, $nombre"
    }

    private fun cargarContadores() {
        tvCountPendientes.text = ordenRepository.contarOrdenesPorEstado("PENDIENTE").toString()
        tvCountEnProceso.text = ordenRepository.contarOrdenesPorEstado("EN PROCESO").toString()
        tvCountFinalizadas.text = ordenRepository.contarOrdenesPorEstado("FINALIZADA").toString()
    }

    private fun cerrarSesion() {
        val prefs = requireContext().getSharedPreferences("ClimaTrackPrefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }
}
