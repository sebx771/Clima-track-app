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
import com.example.mantenimiento.security.AccessControl
import com.example.mantenimiento.security.SessionManager
import com.google.android.material.button.MaterialButton

import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardFragment : Fragment() {

    private lateinit var ordenRepository: OrdenRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var tvSaludo: TextView
    private lateinit var tvCountPendientes: TextView
    private lateinit var tvCountEnProceso: TextView
    private lateinit var tvCountFinalizadas: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        sessionManager = SessionManager(requireContext())
        ordenRepository = OrdenRepository(requireContext())
        
        tvSaludo = view.findViewById(R.id.tvSaludo)
        tvCountPendientes = view.findViewById(R.id.tvCountPendientes)
        tvCountEnProceso = view.findViewById(R.id.tvCountEnProceso)
        tvCountFinalizadas = view.findViewById(R.id.tvCountFinalizadas)

        cargarDatosUsuario()
        cargarContadores()

        // Navegación entre pestañas
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val role = sessionManager.getUserRole()

        val btnOrdenes = view.findViewById<MaterialButton>(R.id.btnVerOrdenes)
        if (AccessControl.canViewOrders(role)) {
            btnOrdenes.setOnClickListener {
                bottomNav.selectedItemId = R.id.nav_ordenes
            }
        } else {
            btnOrdenes.visibility = View.GONE
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
        tvSaludo.text = "Hola, ${sessionManager.getUserName()}"
    }

    private fun cargarContadores() {
        val role = sessionManager.getUserRole()
        val userId = requireContext().getSharedPreferences("ClimaTrackPrefs", Context.MODE_PRIVATE).getInt("userId", -1)

        if (role == com.example.mantenimiento.security.Role.TECNICO) {
            // El técnico solo ve sus contadores asignados
            // Aquí deberíamos tener métodos en repo que filtren por técnico
            // Por simplicidad ahora usamos los globales o podemos filtrar la lista completa
            tvCountPendientes.text = ordenRepository.obtenerOrdenesAsignadas(userId).count { it.estado == "PENDIENTE" }.toString()
            tvCountEnProceso.text = ordenRepository.obtenerOrdenesAsignadas(userId).count { it.estado == "EN PROCESO" }.toString()
            tvCountFinalizadas.text = ordenRepository.obtenerOrdenesAsignadas(userId).count { it.estado == "FINALIZADA" }.toString()
        } else if (role == com.example.mantenimiento.security.Role.CLIENTE) {
            // El cliente ve el resumen de sus órdenes finalizadas vs pendientes
            val empresa = sessionManager.getEmpresaCliente() ?: "ACME S.A.S"
            val todas = ordenRepository.obtenerOrdenes()
            val deCliente = todas.filter { it.clienteNombre == empresa }
            
            tvCountPendientes.text = deCliente.count { it.estado == "PENDIENTE" }.toString()
            tvCountEnProceso.text = deCliente.count { it.estado == "EN PROCESO" }.toString()
            tvCountFinalizadas.text = deCliente.count { it.estado == "FINALIZADA" }.toString()
        } else {
            // Admin ve todo
            tvCountPendientes.text = ordenRepository.contarOrdenesPorEstado("PENDIENTE").toString()
            tvCountEnProceso.text = ordenRepository.contarOrdenesPorEstado("EN PROCESO").toString()
            tvCountFinalizadas.text = ordenRepository.contarOrdenesPorEstado("FINALIZADA").toString()
        }
    }

    private fun cerrarSesion() {
        sessionManager.logout()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }
}
