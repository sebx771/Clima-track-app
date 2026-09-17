package com.example.mantenimiento.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Cliente
import com.example.mantenimiento.models.Equipo
import com.example.mantenimiento.models.Orden
import com.example.mantenimiento.repository.ClienteRepository
import com.example.mantenimiento.repository.EquipoRepository
import com.example.mantenimiento.repository.OrdenRepository
import com.example.mantenimiento.security.AccessControl
import com.example.mantenimiento.security.SessionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Locale

class FormOrdenActivity : AppCompatActivity() {

    private lateinit var repo: OrdenRepository
    private lateinit var clienteRepo: ClienteRepository
    private lateinit var equipoRepo: EquipoRepository
    private lateinit var sessionManager: SessionManager

    private var listaClientes: List<Cliente> = emptyList()
    private var listaEquipos: List<Equipo> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        if (!AccessControl.canRequestService(sessionManager.getUserRole())) {
            Toast.makeText(this, "Acceso no autorizado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContentView(R.layout.activity_form_orden)

        repo = OrdenRepository(this)
        clienteRepo = ClienteRepository(this)
        equipoRepo = EquipoRepository(this)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbarFormOrden)
        toolbar.setNavigationOnClickListener { finish() }

        setupForm()

        findViewById<MaterialButton>(R.id.btnGuardarOrden).setOnClickListener {
            saveOrden()
        }
    }

    private fun setupForm() {
        val role = sessionManager.getUserRole()
        
        // Fecha
        val etFecha = findViewById<TextInputEditText>(R.id.etFechaOrden)
        etFecha.setOnClickListener { showDatePicker() }

        // Tipos de servicio
        val tipos = arrayOf("PREVENTIVO", "CORRECTIVO", "INSTALACION", "DIAGNOSTICO")
        val adapterTipos = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tipos)
        findViewById<AutoCompleteTextView>(R.id.spinnerTipoOrden).setAdapter(adapterTipos)

        val etClienteNombre = findViewById<AutoCompleteTextView>(R.id.etClienteNombre)
        val etEquipoNombre = findViewById<AutoCompleteTextView>(R.id.etEquipoNombre)

        if (role == com.example.mantenimiento.security.Role.CLIENTE) {
            val userId = sessionManager.getUserId()
            
            // Generar número de OT
            val etNumeroOT = findViewById<TextInputEditText>(R.id.etNumeroOT)
            etNumeroOT.setText("SOL-${System.currentTimeMillis()}")
            etNumeroOT.isEnabled = false

            // Cargar datos de Cliente
            val clienteName = sessionManager.getEmpresaCliente() ?: "Cliente"
            listaClientes = clienteRepo.getAllClientes() // Necesitamos la lista para obtener el ID luego
            etClienteNombre.setText(clienteName, false)
            etClienteNombre.isEnabled = false

            // Filtrar equipos
            listaEquipos = equipoRepo.getEquiposByCliente(userId)
            val nombresEquipos = listaEquipos.map { "${it.marca} ${it.modelo} (${it.codigo})" }
            val adapterEquipos = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombresEquipos)
            etEquipoNombre.setAdapter(adapterEquipos)

            val passedEquipoId = intent.getIntExtra("EQUIPO_ID", -1)
            if (passedEquipoId != -1) {
                val eq = listaEquipos.find { it.id == passedEquipoId }
                if (eq != null) {
                    etEquipoNombre.setText("${eq.marca} ${eq.modelo} (${eq.codigo})", false)
                }
            }

        } else {
            // Cargar Sugerencias de Clientes
            listaClientes = clienteRepo.getAllClientes()
            val nombresClientes = listaClientes.map { it.nombre }
            val adapterClientes = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombresClientes)
            etClienteNombre.setAdapter(adapterClientes)

            // Cargar Sugerencias de Equipos
            listaEquipos = equipoRepo.getAllEquipos()
            val nombresEquipos = listaEquipos.map { "${it.marca} ${it.modelo} (${it.codigo})" }
            val adapterEquipos = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombresEquipos)
            etEquipoNombre.setAdapter(adapterEquipos)
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(this, { _, y, m, d ->
            val seleccionada = Calendar.getInstance()
            seleccionada.set(y, m, d)
            
            val dayOfWeek = seleccionada.get(Calendar.DAY_OF_WEEK)
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                Toast.makeText(this, "No se agendan mantenimientos los fines de semana", Toast.LENGTH_SHORT).show()
            } else {
                val fechaFormateada = String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y)
                findViewById<TextInputEditText>(R.id.etFechaOrden).setText(fechaFormateada)
            }
        }, year, month, day)

        // No permitir fechas pasadas
        dialog.datePicker.minDate = calendar.timeInMillis
        
        // Máximo 60 días a futuro
        calendar.add(Calendar.DAY_OF_YEAR, 60)
        dialog.datePicker.maxDate = calendar.timeInMillis
        
        dialog.show()
    }

    private fun saveOrden() {
        val numero = findViewById<TextInputEditText>(R.id.etNumeroOT).text.toString().trim()
        val fecha = findViewById<TextInputEditText>(R.id.etFechaOrden).text.toString().trim()
        val clienteSeleccionado = findViewById<AutoCompleteTextView>(R.id.etClienteNombre).text.toString().trim()
        val equipoSeleccionado = findViewById<AutoCompleteTextView>(R.id.etEquipoNombre).text.toString().trim()
        val tipo = findViewById<AutoCompleteTextView>(R.id.spinnerTipoOrden).text.toString()
        val desc = findViewById<TextInputEditText>(R.id.etDescripcionOrden).text.toString().trim()

        if (numero.isEmpty() || fecha.isEmpty() || clienteSeleccionado.isEmpty() || equipoSeleccionado.isEmpty() || tipo.isEmpty()) {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación de longitud de descripción
        if (desc.length < 20) {
            Toast.makeText(this, "La descripción debe tener al menos 20 caracteres para mayor claridad técnica", Toast.LENGTH_LONG).show()
            return
        }

        // Buscar ID del Cliente
        val cliente = listaClientes.find { it.nombre == clienteSeleccionado }
        if (cliente == null) {
            Toast.makeText(this, "Debe seleccionar un cliente existente", Toast.LENGTH_SHORT).show()
            return
        }

        // Buscar ID del Equipo
        val equipo = listaEquipos.find { "${it.marca} ${it.modelo} (${it.codigo})" == equipoSeleccionado }
        if (equipo == null) {
            Toast.makeText(this, "Debe seleccionar un equipo existente", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación de Duplicidad y Órdenes Activas
        if (repo.existeOrdenEnFecha(equipo.id!!, fecha)) {
            Toast.makeText(this, "Ya existe una solicitud para este equipo en la fecha seleccionada", Toast.LENGTH_LONG).show()
            return
        }

        if (repo.existeOrdenActiva(equipo.id!!)) {
            Toast.makeText(this, "El equipo ya tiene una orden activa (Pendiente o En Proceso)", Toast.LENGTH_LONG).show()
            return
        }

        val nuevaOrden = Orden(
            id = null,
            numero = numero,
            fecha = fecha,
            clienteId = cliente.id!!,
            equipoId = equipo.id!!,
            tecnicoId = null,
            tipoServicio = tipo,
            descripcion = desc,
            estado = "PENDIENTE"
        )

        val id = repo.addOrden(nuevaOrden)
        if (id > 0) {
            Toast.makeText(this, "Orden creada con éxito", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al guardar orden", Toast.LENGTH_SHORT).show()
        }
    }
}
