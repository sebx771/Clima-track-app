package com.example.mantenimiento.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Orden
import com.example.mantenimiento.repository.OrdenRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Locale

class FormOrdenActivity : AppCompatActivity() {

    private lateinit var repo: OrdenRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_orden)

        repo = OrdenRepository(this)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbarFormOrden)
        toolbar.setNavigationOnClickListener { finish() }

        setupForm()

        findViewById<MaterialButton>(R.id.btnGuardarOrden).setOnClickListener {
            saveOrden()
        }
    }

    private fun setupForm() {
        // Configurar selector de fecha
        val etFecha = findViewById<TextInputEditText>(R.id.etFechaOrden)
        etFecha.setOnClickListener { showDatePicker() }

        // Configurar dropdown de tipos (Igual que en OrdenesFragment)
        val tipos = arrayOf("PREVENTIVO", "CORRECTIVO", "INSTALACION", "DIAGNOSTICO")
        val adapterTipos = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tipos)
        findViewById<AutoCompleteTextView>(R.id.spinnerTipoOrden).setAdapter(adapterTipos)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
            val fechaFormateada = String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y)
            findViewById<TextInputEditText>(R.id.etFechaOrden).setText(fechaFormateada)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun saveOrden() {
        val numero = findViewById<TextInputEditText>(R.id.etNumeroOT).text.toString()
        val fecha = findViewById<TextInputEditText>(R.id.etFechaOrden).text.toString()
        val cliente = findViewById<TextInputEditText>(R.id.etClienteOrden).text.toString()
        val direccion = findViewById<TextInputEditText>(R.id.etDireccionOrden).text.toString()
        val equipo = findViewById<TextInputEditText>(R.id.etEquipoOrden).text.toString()
        val tipo = findViewById<AutoCompleteTextView>(R.id.spinnerTipoOrden).text.toString()
        val desc = findViewById<TextInputEditText>(R.id.etDescripcionOrden).text.toString()

        if (numero.isEmpty() || fecha.isEmpty() || cliente.isEmpty() || equipo.isEmpty() || tipo.isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_campos_obligatorios), Toast.LENGTH_SHORT).show()
            return
        }

        val nuevaOrden = Orden(
            id = 0, // El ID se autogenera en DB
            numero = numero,
            fecha = fecha,
            cliente = cliente,
            direccion = direccion,
            equipo = equipo,
            tipoServicio = tipo,
            descripcion = desc,
            estado = "PENDIENTE"
        )

        val id = repo.addOrden(nuevaOrden)
        if (id > 0) {
            Toast.makeText(this, getString(R.string.msg_orden_exito), Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, getString(R.string.msg_orden_error), Toast.LENGTH_SHORT).show()
        }
    }
}