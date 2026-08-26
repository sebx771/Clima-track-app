package com.example.mantenimiento.fragments

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
import com.example.mantenimiento.adapters.OrdenesAdapter
import com.example.mantenimiento.repository.OrdenRepository

class OrdenesFragment : Fragment() {

    private lateinit var ordenRepository: OrdenRepository
    private lateinit var ordenesAdapter: OrdenesAdapter
    private lateinit var rvOrdenes: RecyclerView
    private lateinit var spinnerEstado: Spinner
    private lateinit var spinnerTipo: Spinner

    private val opcionesEstado = arrayOf("TODOS", "PENDIENTE", "EN PROCESO", "CANCELADA") // Quitamos FINALIZADA
    private val opcionesTipo = arrayOf("TODOS", "PREVENTIVO", "CORRECTIVO", "INSTALACION", "DIAGNOSTICO")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_ordenes, container, false)

        ordenRepository = OrdenRepository(requireContext())
        rvOrdenes = view.findViewById(R.id.rvOrdenes)
        spinnerEstado = view.findViewById(R.id.spinnerEstado)
        spinnerTipo = view.findViewById(R.id.spinnerTipo)

        setupRecyclerView()
        setupSpinners()

        return view
    }

    private fun setupRecyclerView() {
        ordenesAdapter = OrdenesAdapter(emptyList()) { orden ->
            Toast.makeText(requireContext(), "Orden: ${orden.numero}", Toast.LENGTH_SHORT).show()
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
        val estado = spinnerEstado.selectedItem.toString()
        val tipo = spinnerTipo.selectedItem.toString()

        // Si es TODOS, traemos activas (que excluye FINALIZADA)
        val lista = if (estado == "TODOS") {
             ordenRepository.obtenerOrdenesActivas()
        } else {
             ordenRepository.obtenerOrdenesFiltradas(estado, tipo)
        }
        
        ordenesAdapter.actualizarLista(lista)
    }

    override fun onResume() {
        super.onResume()
        aplicarFiltros()
    }
}
