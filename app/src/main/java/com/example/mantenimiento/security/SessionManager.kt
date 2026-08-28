package com.example.mantenimiento.security

import android.content.Context
import android.content.SharedPreferences
import com.example.mantenimiento.models.Usuario

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("ClimaTrackPrefs", Context.MODE_PRIVATE)

    fun saveSession(usuario: Usuario) {
        prefs.edit().apply {
            putBoolean("isLoggedIn", true)
            putInt("userId", usuario.id)
            putString("usuario", usuario.usuario)
            putString("nombre", usuario.nombre)
            putString("email", usuario.email)
            putString("rol", usuario.rol)
            // Para clientes, asumimos que el nombre del técnico guardado es su identificador de empresa
            // En una app real, esto vendría de una relación en la DB
            if (usuario.rol.equals("Cliente", ignoreCase = true)) {
                putString("empresaCliente", "ACME S.A.S") // Valor de prueba
            }
            apply()
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean("isLoggedIn", false)

    fun getUserRole(): Role = Role.fromString(prefs.getString("rol", "Técnico"))

    fun getUserName(): String = prefs.getString("nombre", "Usuario") ?: "Usuario"
    
    fun getEmpresaCliente(): String? = prefs.getString("empresaCliente", null)

    fun logout() {
        prefs.edit().clear().apply()
    }
}
