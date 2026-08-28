package com.example.mantenimiento.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Usuario
import com.example.mantenimiento.repository.OrdenRepository
import com.example.mantenimiento.repository.UsuarioRepository
import com.google.android.material.button.MaterialButton

class AsignarOrdenActivity : AppCompatActivity() {

    private lateinit var repoOrden: OrdenRepository
    private lateinit var repoUser: UsuarioRepository
    private var ordenId: Int = -1
    private var tecnicos: List<Usuario> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignar_orden)

        repoOrden = OrdenRepository(this)
        repoUser = UsuarioRepository(this)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbarAsign)
        toolbar.setNavigationOnClickListener { finish() }

        ordenId = intent.getIntExtra("ORDEN_ID", -1)
        val numOT = intent.getStringExtra("ORDEN_NUMERO") ?: "OT-???"
        val cliente = intent.getStringExtra("ORDEN_CLIENTE") ?: "..."

        findViewById<TextView>(R.id.tvNumOTAsign).text = "Orden: $numOT"
        findViewById<TextView>(R.id.tvClienteOTAsign).text = "Cliente: $cliente"

        setupSpinner()

        findViewById<MaterialButton>(R.id.btnConfirmarAsign).setOnClickListener {
            confirmarAsignacion()
        }
    }

    private fun setupSpinner() {
        tecnicos = repoUser.getTecnicos()
        val nombres = tecnicos.map { it.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombres)
        findViewById<AutoCompleteTextView>(R.id.spinnerTecnicos).setAdapter(adapter)
    }

    private fun confirmarAsignacion() {
        val seleccion = findViewById<AutoCompleteTextView>(R.id.spinnerTecnicos).text.toString()
        val tecnico = tecnicos.find { it.nombre == seleccion }

        if (tecnico == null) {
            Toast.makeText(this, "Selecciona un técnico válido", Toast.LENGTH_SHORT).show()
            return
        }

        val result = repoOrden.asignarTecnico(ordenId, tecnico.id)
        if (result > 0) {
            Toast.makeText(this, "Orden asignada a ${tecnico.nombre}", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al asignar", Toast.LENGTH_SHORT).show()
        }
    }
}
