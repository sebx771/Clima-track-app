package com.example.mantenimiento.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Cliente
import com.example.mantenimiento.repository.ClienteRepository
import com.example.mantenimiento.security.AccessControl
import com.example.mantenimiento.security.SessionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class FormClienteActivity : AppCompatActivity() {

    private lateinit var repo: ClienteRepository
    private lateinit var sessionManager: SessionManager
    private var clienteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        if (!AccessControl.canManageInventory(sessionManager.getUserRole())) {
            Toast.makeText(this, "Acceso no autorizado", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        setContentView(R.layout.activity_form_cliente)

        repo = ClienteRepository(this)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbarFormCliente)
        toolbar.setNavigationOnClickListener { finish() }

        clienteId = intent.getIntExtra("CLIENTE_ID", -1)
        if (clienteId != -1) {
            toolbar.title = "Editar Cliente"
            loadClienteData()
        }

        findViewById<MaterialButton>(R.id.btnGuardarCliente).setOnClickListener {
            saveCliente()
        }
    }

    private fun loadClienteData() {
        val cliente = repo.getClienteById(clienteId)
        cliente?.let {
            findViewById<TextInputEditText>(R.id.etNombreCliente).setText(it.nombre)
            findViewById<TextInputEditText>(R.id.etTelefonoCliente).setText(it.telefono)
            findViewById<TextInputEditText>(R.id.etDireccionCliente).setText(it.direccion)
            findViewById<TextInputEditText>(R.id.etEmailCliente).setText(it.email)
        }
    }

    private fun saveCliente() {
        val nombre = findViewById<TextInputEditText>(R.id.etNombreCliente).text.toString().trim()
        val tel = findViewById<TextInputEditText>(R.id.etTelefonoCliente).text.toString().trim()
        val dir = findViewById<TextInputEditText>(R.id.etDireccionCliente).text.toString().trim()
        val email = findViewById<TextInputEditText>(R.id.etEmailCliente).text.toString().trim()

        if (nombre.isEmpty() || tel.isEmpty() || dir.isEmpty()) {
            Toast.makeText(this, "Nombre, Teléfono y Dirección son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val cliente = Cliente(
            id = if (clienteId != -1) clienteId else null,
            nombre = nombre,
            telefono = tel,
            direccion = dir,
            email = email
        )

        val result = if (clienteId != -1) {
            repo.updateCliente(cliente).toLong()
        } else {
            repo.addCliente(cliente)
        }

        if (result > 0) {
            Toast.makeText(this, "Cliente guardado correctamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al guardar cliente", Toast.LENGTH_SHORT).show()
        }
    }
}
