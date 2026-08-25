package com.example.mantenimiento.activities

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mantenimiento.adapters.OrdenesAdapter
import com.example.mantenimiento.databinding.ActivityOrdenesBinding
import com.example.mantenimiento.repository.OrdenRepository

class OrdenesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdenesBinding
    private lateinit var ordenRepository: OrdenRepository
    private lateinit var ordenesAdapter: OrdenesAdapter

    // Opciones para los filtros
    private val opcionesEstado = arrayOf("TODOS", "PENDIENTE", "EN PROCESO", "FINALIZADA", "CANCELADA")
    private val opcionesTipo = arrayOf("TODOS", "PREVENTIVO", "CORRECTIVO", "INSTALACION", "DIAGNOSTICO")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdenesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.navInicio.setOnClickListener {
            finish()
        }

        binding.navEquipos.setOnClickListener {
            startActivity(Intent(this, FormEquipoActivity::class.java))
        }

        ordenRepository = OrdenRepository(this)

        setupRecyclerView()
        setupSpinners()
        cargarDatos()
    }

    private fun setupRecyclerView() {
        // Inicializamos el adapter con una lista vacía y la acción al hacer clic
        ordenesAdapter = OrdenesAdapter(emptyList()) { ordenSeleccionada ->
            Toast.makeText(this, "Seleccionaste: ${ordenSeleccionada.numero}", Toast.LENGTH_SHORT).show()
            // Aquí luego conectas con el módulo de tu compañero (Detalle / Registro de Trabajo)
        }

        binding.rvOrdenes.apply {
            layoutManager = LinearLayoutManager(this@OrdenesActivity)
            adapter = ordenesAdapter
        }
    }

    private fun setupSpinners() {
        // Cargar opciones en el Spinner de Estado
        val adapterEstado = ArrayAdapter(this, R.layout.simple_spinner_item, opcionesEstado)
        adapterEstado.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerEstado.adapter = adapterEstado

        // Cargar opciones en el Spinner de Tipo
        val adapterTipo = ArrayAdapter(this, R.layout.simple_spinner_item, opcionesTipo)
        adapterTipo.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerTipo.adapter = adapterTipo

        // Listener para detectar cambios en las selecciones de los filtros
        val filterListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                aplicarFiltros()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerEstado.onItemSelectedListener = filterListener
        binding.spinnerTipo.onItemSelectedListener = filterListener
    }

    private fun aplicarFiltros() {
        val estadoSeleccionado = binding.spinnerEstado.selectedItem.toString()
        val tipoSeleccionado = binding.spinnerTipo.selectedItem.toString()

        // Consultar SQLite con los filtros actuales
        val ordenesFiltradas = ordenRepository.obtenerOrdenesFiltradas(estadoSeleccionado, tipoSeleccionado)

        // Actualizar el RecyclerView
        ordenesAdapter.actualizarLista(ordenesFiltradas)
    }

    private fun cargarDatos() {
        aplicarFiltros()
    }
}