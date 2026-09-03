package com.example.mantenimiento.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu // <-- Nueva importación
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
        // Navegación entre pestañas
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val role = sessionManager.getUserRole()

        // =========================================================
        // NUEVO: Ocultar ítems SOLO en la barra de navegación inferior
        // =========================================================
        bottomNav.menu.findItem(R.id.nav_usuarios)?.isVisible = false
        bottomNav.menu.findItem(R.id.nav_clientes)?.isVisible = false

        // =========================================================
        // NUEVO CÓDIGO: Configuración del menú Popup superior
        // =========================================================
        val ivMenuOpciones = view.findViewById<ImageView>(R.id.ivMenuOpciones)

        ivMenuOpciones.setOnClickListener { v ->
            val popupMenu = PopupMenu(requireContext(), v)
            popupMenu.menuInflater.inflate(R.menu.bottom_nav_menu, popupMenu.menu)

            // =========================================================
            // NUEVO: Control de acceso para la opción de Usuarios
            // =========================================================
            val itemUsuarios = popupMenu.menu.findItem(R.id.nav_usuarios)

            // Si tienes un método en AccessControl (ej: AccessControl.canViewUsers(role)), úsalo.
            // De lo contrario, ocultamos el ítem manualmente para Técnicos y Clientes:
            if (role == com.example.mantenimiento.security.Role.TECNICO ||
                role == com.example.mantenimiento.security.Role.CLIENTE) {
                itemUsuarios?.isVisible = false
            }
            // =========================================================

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_inicio -> {
                        bottomNav.selectedItemId = R.id.nav_inicio
                        true
                    }
                    R.id.nav_ordenes -> {
                        bottomNav.selectedItemId = R.id.nav_ordenes
                        true
                    }
                    R.id.nav_equipos -> {
                        bottomNav.selectedItemId = R.id.nav_equipos
                        true
                    }
                    R.id.nav_historial -> {
                        bottomNav.selectedItemId = R.id.nav_historial
                        true
                    }
                    R.id.nav_usuarios -> {
                        bottomNav.selectedItemId = R.id.nav_usuarios
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
        // =========================================================

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
            tvCountPendientes.text = ordenRepository.obtenerOrdenesAsignadas(userId).count { it.estado == "PENDIENTE" }.toString()
            tvCountEnProceso.text = ordenRepository.obtenerOrdenesAsignadas(userId).count { it.estado == "EN PROCESO" }.toString()
            tvCountFinalizadas.text = ordenRepository.obtenerOrdenesAsignadas(userId).count { it.estado == "FINALIZADA" }.toString()
        } else if (role == com.example.mantenimiento.security.Role.CLIENTE) {
            // Como sesión maneja la empresa o ID, filtramos las órdenes del cliente actual
            val empresa = sessionManager.getEmpresaCliente() ?: "ACME S.A.S"
            val todas = ordenRepository.obtenerOrdenes()

            // Filtramos usando el campo auxiliar clienteNombre o el ID según corresponda
            val deCliente = todas.filter { it.clienteNombre == empresa }

            tvCountPendientes.text = deCliente.count { it.estado == "PENDIENTE" }.toString()
            tvCountEnProceso.text = deCliente.count { it.estado == "EN PROCESO" }.toString()
            tvCountFinalizadas.text = deCliente.count { it.estado == "FINALIZADA" }.toString()
        } else {
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