package com.example.mantenimiento.repository

import android.content.Context
import com.example.mantenimiento.database.DatabaseHelper
import com.example.mantenimiento.models.Usuario

class UsuarioRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun validarCredenciales(userInput: String, passwordInput: String): Usuario? {
        val db = dbHelper.readableDatabase

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

            usuarioEncontrado = Usuario(id, usuario, email, nombre, rol)
        }

        cursor.close()
        return usuarioEncontrado
    }
}