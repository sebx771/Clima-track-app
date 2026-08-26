package com.example.mantenimiento.repository

import android.content.Context
import android.util.Log
import com.example.mantenimiento.database.DatabaseHelper
import com.example.mantenimiento.models.Usuario

class UsuarioRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun validarCredenciales(userInput: String, passwordInput: String): Usuario? {
        val db = dbHelper.readableDatabase
        Log.d("UsuarioRepository", "Intentando validar: $userInput")

        // Permite ingresar con 'tecnico01' O 'tecnico01@climatrack.com'
        val query = """
            SELECT * FROM ${DatabaseHelper.TABLE_USUARIOS} 
            WHERE (${DatabaseHelper.KEY_USR_USUARIO} = ? OR ${DatabaseHelper.KEY_USR_EMAIL} = ?) 
            AND ${DatabaseHelper.KEY_USR_PASSWORD} = ?
        """.trimIndent()

        // Pasamos userInput 2 veces (una para usuario y otra para email)
        val cursor = db.rawQuery(query, arrayOf(userInput, userInput, passwordInput))

        var usuarioEncontrado: Usuario? = null

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USR_ID))
            val usuario = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USR_USUARIO))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USR_EMAIL))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USR_NOMBRE))
            val rol = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USR_ROL))

            Log.d("UsuarioRepository", "Usuario encontrado: $usuario")
            usuarioEncontrado = Usuario(id, usuario, nombre, email, rol)
        } else {
            Log.w("UsuarioRepository", "No se encontró el usuario o contraseña incorrecta")
        }

        cursor.close()
        return usuarioEncontrado
    }
}