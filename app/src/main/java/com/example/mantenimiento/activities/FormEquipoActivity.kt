package com.example.mantenimiento.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Equipo
import com.example.mantenimiento.repository.EquipoRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class FormEquipoActivity : AppCompatActivity() {

    private lateinit var repo: EquipoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_equipo)

        repo = EquipoRepository(this)

        findViewById<MaterialButton>(R.id.btnGuardarEquipo).setOnClickListener {
            saveEquipo()
        }
    }

    private fun saveEquipo() {
        val nombre = findViewById<TextInputEditText>(R.id.etNombre).text.toString()
        val marca = findViewById<TextInputEditText>(R.id.etMarca).text.toString()
        val modelo = findViewById<TextInputEditText>(R.id.etModelo).text.toString()
        val serie = findViewById<TextInputEditText>(R.id.etSerie).text.toString()
        val ubicacion = findViewById<TextInputEditText>(R.id.etUbicacion).text.toString()
        val cliente = findViewById<TextInputEditText>(R.id.etCliente).text.toString()

        if (nombre.isEmpty() || marca.isEmpty()) {
            Toast.makeText(this, "Nombre y Marca son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val equipo = Equipo(
            nombre = nombre,
            marca = marca,
            modelo = modelo,
            numeroSerie = serie,
            ubicacion = ubicacion,
            cliente = cliente,
        )

        val id = repo.addEquipo(equipo)
        if (id > 0) {
            Toast.makeText(this, "Equipo guardado correctamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al guardar equipo", Toast.LENGTH_SHORT).show()
        }
    }
}
