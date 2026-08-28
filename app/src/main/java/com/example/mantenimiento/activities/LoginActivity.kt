package com.example.mantenimiento.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimiento.MainActivity
import com.example.mantenimiento.databinding.ActivityLoginBinding
import com.example.mantenimiento.repository.UsuarioRepository
import com.example.mantenimiento.security.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var usuarioRepository: UsuarioRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        if (sessionManager.isLoggedIn()) {
            irAlDashboard()
            return
        }

        usuarioRepository = UsuarioRepository(this)


        binding.btnIngresar.setOnClickListener {
            val inputUserOrEmail = binding.etUsuario.text.toString().trim()
            val inputPassword = binding.etPassword.text.toString().trim()


            if (inputUserOrEmail.isEmpty()) {
                binding.etUsuario.error = "Ingrese su usuario o correo"
                return@setOnClickListener
            }

            if (inputPassword.isEmpty()) {
                binding.etPassword.error = "Ingrese su contraseña"
                return@setOnClickListener
            }

            val usuarioValido = usuarioRepository.validarCredenciales(inputUserOrEmail, inputPassword)

            if (usuarioValido != null) {
                sessionManager.saveSession(usuarioValido)
                Toast.makeText(this, "Bienvenido ${usuarioValido.nombre}", Toast.LENGTH_SHORT).show()
                irAlDashboard()
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun irAlDashboard() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}