package com.example.mantenimiento.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Repuesto
import com.example.mantenimiento.repository.RepuestoRepository
import com.example.mantenimiento.security.AccessControl
import com.example.mantenimiento.security.SessionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class FormRepuestoActivity : AppCompatActivity() {

    private lateinit var repo: RepuestoRepository
    private lateinit var sessionManager: SessionManager
    private var repuestoId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        if (!AccessControl.canManageInventory(sessionManager.getUserRole())) {
            Toast.makeText(this, "Acceso denegado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContentView(R.layout.activity_form_repuesto)

        repo = RepuestoRepository(this)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbarFormRepuesto)
        toolbar.setNavigationOnClickListener { finish() }

        repuestoId = intent.getIntExtra("REPUESTO_ID", -1)
        if (repuestoId != -1) {
            toolbar.title = "Editar Repuesto"
            loadRepuestoData()
        }

        findViewById<MaterialButton>(R.id.btnGuardarRepuesto).setOnClickListener {
            saveRepuesto()
        }
    }

    private fun loadRepuestoData() {
        val repuesto = repo.getRepuestoById(repuestoId)
        repuesto?.let {
            findViewById<TextInputEditText>(R.id.etNombreRepuesto).setText(it.nombre)
            findViewById<TextInputEditText>(R.id.etCodigoRepuesto).setText(it.codigo)
            findViewById<TextInputEditText>(R.id.etStockRepuesto).setText(it.cantidadDisponible.toString())
        }
    }

    private fun saveRepuesto() {
        val nombre = findViewById<TextInputEditText>(R.id.etNombreRepuesto).text.toString()
        val codigo = findViewById<TextInputEditText>(R.id.etCodigoRepuesto).text.toString()
        val stockStr = findViewById<TextInputEditText>(R.id.etStockRepuesto).text.toString()

        if (nombre.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_campos_obligatorios), Toast.LENGTH_SHORT).show()
            return
        }

        val stock = stockStr.toIntOrNull() ?: 0
        val repuesto = Repuesto(
            id = if (repuestoId != -1) repuestoId else null,
            nombre = nombre,
            codigo = codigo,
            cantidadDisponible = stock,
        )

        val result = if (repuestoId != -1) {
            repo.updateRepuesto(repuesto).toLong()
        } else {
            repo.addRepuesto(repuesto)
        }

        if (result > 0) {
            Toast.makeText(this, getString(R.string.msg_repuesto_guardado), Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, getString(R.string.msg_repuesto_error), Toast.LENGTH_SHORT).show()
        }
    }
}
