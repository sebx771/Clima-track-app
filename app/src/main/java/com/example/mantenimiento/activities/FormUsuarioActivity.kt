package com.example.mantenimiento.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.R
import com.example.mantenimiento.models.Usuario
import com.example.mantenimiento.repository.UsuarioRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class FormUsuarioActivity : AppCompatActivity() {

    private lateinit var repo: UsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_usuario)

        repo = UsuarioRepository(this)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbarFormUser)
        toolbar.setNavigationOnClickListener { finish() }

        setupForm()

        findViewById<MaterialButton>(R.id.btnGuardarUser).setOnClickListener {
            saveUser()
        }
    }

    private fun setupForm() {
        val roles = arrayOf("Administrador", "Técnico")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        findViewById<AutoCompleteTextView>(R.id.spinnerRolUser).setAdapter(adapter)
    }

    private fun saveUser() {
        val nombre = findViewById<TextInputEditText>(R.id.etNombreUser).text.toString().trim()
        val userLogin = findViewById<TextInputEditText>(R.id.etUsuarioLogin).text.toString().trim()
        val email = findViewById<TextInputEditText>(R.id.etEmailUser).text.toString().trim()
        val pass = findViewById<TextInputEditText>(R.id.etPassUser).text.toString().trim()
        val rol = findViewById<AutoCompleteTextView>(R.id.spinnerRolUser).text.toString()

        if (nombre.isEmpty() || userLogin.isEmpty() || email.isEmpty() || pass.isEmpty() || rol.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val usuario = Usuario(
            id = 0,
            usuario = userLogin,
            nombre = nombre,
            email = email,
            rol = rol
        )

        val id = repo.addUsuario(usuario, pass)
        if (id > 0) {
            Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
        }
    }
}
