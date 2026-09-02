package com.example.mantenimiento.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Cliente
import com.example.mantenimiento.models.Equipo
import com.example.mantenimiento.repository.ClienteRepository
import com.example.mantenimiento.repository.EquipoRepository
import com.example.mantenimiento.security.AccessControl
import com.example.mantenimiento.security.SessionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class FormEquipoActivity : AppCompatActivity() {

    private lateinit var repo: EquipoRepository
    private lateinit var clienteRepo: ClienteRepository
    private lateinit var sessionManager: SessionManager
    private var equipoId: Int = -1
    private var listaClientes: List<Cliente> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        if (!AccessControl.canCreateEquipment(sessionManager.getUserRole())) {
            Toast.makeText(this, "Acceso no autorizado", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        setContentView(R.layout.activity_form_equipo)

        repo = EquipoRepository(this)
        clienteRepo = ClienteRepository(this)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbarFormEquipo)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        setupClientSuggestions()

        equipoId = intent.getIntExtra("EQUIPO_ID", -1)
        if (equipoId != -1) {
            toolbar.title = "Editar Equipo"
            loadEquipoData()
        }

        findViewById<MaterialButton>(R.id.btnGuardarEquipo).setOnClickListener {
            saveEquipo()
        }
    }

    private fun setupClientSuggestions() {
        listaClientes = clienteRepo.getAllClientes()
        val nombres = listaClientes.map { it.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombres)
        findViewById<AutoCompleteTextView>(R.id.etClienteNombre).setAdapter(adapter)
    }

    private fun loadEquipoData() {
        val equipo = repo.getEquipoById(equipoId)
        equipo?.let {
            findViewById<TextInputEditText>(R.id.etCodigo).setText(it.codigo)
            findViewById<TextInputEditText>(R.id.etTipo).setText(it.tipo)
            findViewById<TextInputEditText>(R.id.etMarca).setText(it.marca)
            findViewById<TextInputEditText>(R.id.etModelo).setText(it.modelo)
            findViewById<TextInputEditText>(R.id.etSerie).setText(it.serial)
            findViewById<TextInputEditText>(R.id.etCapacidad).setText(it.capacidad)
            findViewById<TextInputEditText>(R.id.etUbicacion).setText(it.ubicacion)
            
            // Buscar nombre del cliente por ID
            val cliente = listaClientes.find { c -> c.id == it.clienteId }
            findViewById<AutoCompleteTextView>(R.id.etClienteNombre).setText(cliente?.nombre ?: "", false)
        }
    }

    private fun saveEquipo() {
        val codigo = findViewById<TextInputEditText>(R.id.etCodigo).text.toString().trim()
        val tipo = findViewById<TextInputEditText>(R.id.etTipo).text.toString().trim()
        val marca = findViewById<TextInputEditText>(R.id.etMarca).text.toString().trim()
        val modelo = findViewById<TextInputEditText>(R.id.etModelo).text.toString().trim()
        val serie = findViewById<TextInputEditText>(R.id.etSerie).text.toString().trim()
        val capacidad = findViewById<TextInputEditText>(R.id.etCapacidad).text.toString().trim()
        val ubicacion = findViewById<TextInputEditText>(R.id.etUbicacion).text.toString().trim()
        val clienteSeleccionado = findViewById<AutoCompleteTextView>(R.id.etClienteNombre).text.toString().trim()

        if (codigo.isEmpty() || marca.isEmpty() || clienteSeleccionado.isEmpty()) {
            Toast.makeText(this, "Código, Marca y Cliente son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val cliente = listaClientes.find { it.nombre == clienteSeleccionado }
        if (cliente == null) {
            Toast.makeText(this, "Debe seleccionar un cliente existente de la lista", Toast.LENGTH_SHORT).show()
            return
        }

        val equipo = Equipo(
            id = if (equipoId != -1) equipoId else null,
            codigo = codigo,
            tipo = tipo,
            marca = marca,
            modelo = modelo,
            serial = serie,
            capacidad = capacidad,
            ubicacion = ubicacion,
            clienteId = cliente.id!!,
            estado = "OPERATIVO"
        )

        val result = if (equipoId != -1) {
            repo.updateEquipo(equipo).toLong()
        } else {
            repo.addEquipo(equipo)
        }

        if (result > 0) {
            Toast.makeText(this, "Equipo guardado correctamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al guardar equipo", Toast.LENGTH_SHORT).show()
        }
    }
}
