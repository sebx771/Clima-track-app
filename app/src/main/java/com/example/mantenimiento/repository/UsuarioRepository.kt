package com.example.mantenimiento.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.mantenimiento.database.DatabaseHelper
import com.example.mantenimiento.models.Usuario

class UsuarioRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun addUsuario(usuario: Usuario, password: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_USR_USUARIO, usuario.usuario)
            put(DatabaseHelper.KEY_USR_NOMBRE, usuario.nombre)
            put(DatabaseHelper.KEY_USR_EMAIL, usuario.email)
            put(DatabaseHelper.KEY_USR_ROL, usuario.rol)
            put(DatabaseHelper.KEY_USR_PASSWORD, password)
        }
        val id = db.insert(DatabaseHelper.TABLE_USUARIOS, null, values)
        db.close()
        return id
    }

    fun getAllUsuarios(): List<Usuario> {
        val lista = mutableListOf<Usuario>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_USUARIOS}", null)

        if (cursor.moveToFirst()) {
            do {
                lista.add(mapCursorToUsuario(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun deleteUsuario(id: Int): Int {
        val db = dbHelper.writableDatabase
        val result = db.delete(DatabaseHelper.TABLE_USUARIOS, "${DatabaseHelper.KEY_USR_ID}=?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun getTecnicos(): List<Usuario> {
        val lista = mutableListOf<Usuario>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_USUARIOS} WHERE ${DatabaseHelper.KEY_USR_ROL} = ?", arrayOf("Técnico"))

        if (cursor.moveToFirst()) {
            do {
                lista.add(mapCursorToUsuario(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    private fun mapCursorToUsuario(cursor: Cursor): Usuario {
        return Usuario(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USR_ID)),
            usuario = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USR_USUARIO)),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USR_NOMBRE)),
            email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USR_EMAIL)),
            rol = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USR_ROL))
        )
    }

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