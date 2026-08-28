package com.example.mantenimiento.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimiento.R
import com.example.mantenimiento.activities.FormOrdenActivity
import com.example.mantenimiento.activities.AsignarOrdenActivity
import com.example.mantenimiento.adapters.OrdenesAdapter
import com.example.mantenimiento.repository.OrdenRepository
import com.example.mantenimiento.security.AccessControl
import com.example.mantenimiento.security.Role
import com.example.mantenimiento.security.SessionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class OrdenesFragment : Fragment() {

    private lateinit var ordenRepository: OrdenRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var ordenesAdapter: OrdenesAdapter
    private lateinit var rvOrdenes: RecyclerView
    private lateinit var spinnerEstado: Spinner
    private lateinit var spinnerTipo: Spinner
    private lateinit var fabAddOrden: FloatingActionButton

    private val opcionesEstado = arrayOf("TODOS", "PENDIENTE", "EN PROCESO", "CANCELADA") // Quitamos FINALIZADA
    private val opcionesTipo = arrayOf("TODOS", "PREVENTIVO", "CORRECTIVO", "INSTALACION", "DIAGNOSTICO")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sessionManager = SessionManager(requireContext())
        if (!AccessControl.canViewOrders(sessionManager.getUserRole())) {
            Toast.makeText(requireContext(), "Acceso denegado", Toast.LENGTH_SHORT).show()
            return null
        }
        
        val view = inflater.inflate(R.layout.fragment_ordenes, container, false)

        ordenRepository = OrdenRepository(requireContext())
        rvOrdenes = view.findViewById(R.id.rvOrdenes)
        spinnerEstado = view.findViewById(R.id.spinnerEstado)
        spinnerTipo = view.findViewById(R.id.spinnerTipo)
        fabAddOrden = view.findViewById(R.id.fabAddOrden)

        setupRecyclerView()
        setupSpinners()

        if (AccessControl.canManageInventory(sessionManager.getUserRole())) {
            fabAddOrden.setOnClickListener {
                val intent = Intent(requireContext(), FormOrdenActivity::class.java)
                startActivity(intent)
            }
        } else {
            fabAddOrden.visibility = View.GONE
        }

        return view
    }

    private fun setupRecyclerView() {
        val role = sessionManager.getUserRole()
        ordenesAdapter = OrdenesAdapter(emptyList()) { orden ->
            if (role == Role.ADMIN) {
                // El Admin puede asignar la orden
                val intent = Intent(requireContext(), AsignarOrdenActivity::class.java)
                intent.putExtra("ORDEN_ID", orden.id)
                intent.putExtra("ORDEN_NUMERO", orden.numero)
                intent.putExtra("ORDEN_CLIENTE", orden.cliente)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Orden: ${orden.numero}", Toast.LENGTH_SHORT).show()
                // Futuro: El técnico abre el Registro de Mantenimiento
            }
        }
        rvOrdenes.layoutManager = LinearLayoutManager(requireContext())
        rvOrdenes.adapter = ordenesAdapter
    }

    private fun setupSpinners() {
        val adapterEstado = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opcionesEstado)
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = adapterEstado

        val adapterTipo = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opcionesTipo)
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipo.adapter = adapterTipo

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                aplicarFiltros()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        spinnerEstado.onItemSelectedListener = listener
        spinnerTipo.onItemSelectedListener = listener
    }

    private fun aplicarFiltros() {
        val role = sessionManager.getUserRole()
        val estado = spinnerEstado.selectedItem.toString()
        val tipo = spinnerTipo.selectedItem.toString()

        val lista = when {
            role == Role.TECNICO -> {
                // El técnico solo ve sus órdenes asignadas
                val userId = requireContext().getSharedPreferences("ClimaTrackPrefs", Context.MODE_PRIVATE).getInt("userId", -1)
                ordenRepository.obtenerOrdenesAsignadas(userId)
            }
            estado == "TODOS" -> {
                ordenRepository.obtenerOrdenesActivas()
            }
            else -> {
                ordenRepository.obtenerOrdenesFiltradas(estado, tipo)
            }
        }
        
        ordenesAdapter.actualizarLista(lista)
    }

    override fun onResume() {
        super.onResume()
        aplicarFiltros()
    }
}
