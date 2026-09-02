package com.example.mantenimiento.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.mantenimiento.database.DatabaseHelper
import com.example.mantenimiento.models.Cliente

class ClienteRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun getAllClientes(): List<Cliente> {
        val lista = mutableListOf<Cliente>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(DatabaseHelper.TABLE_CLIENTES, null, null, null, null, null, null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    lista.add(mapCursorToCliente(it))
                } while (it.moveToNext())
            }
        }
        db.close()
        return lista
    }

    fun getClienteById(id: Int): Cliente? {
        val db = dbHelper.readableDatabase
        var cliente: Cliente? = null
        val cursor = db.query(DatabaseHelper.TABLE_CLIENTES, null, "${DatabaseHelper.KEY_CLI_ID}=?", arrayOf(id.toString()), null, null, null)

        cursor.use {
            if (it.moveToFirst()) {
                cliente = mapCursorToCliente(it)
            }
        }
        db.close()
        return cliente
    }

    fun addCliente(cliente: Cliente): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_CLI_NOMBRE, cliente.nombre)
            put(DatabaseHelper.KEY_CLI_TEL, cliente.telefono)
            put(DatabaseHelper.KEY_CLI_DIR, cliente.direccion)
            put(DatabaseHelper.KEY_CLI_EMAIL, cliente.email)
        }
        val id = db.insert(DatabaseHelper.TABLE_CLIENTES, null, values)
        db.close()
        return id
    }

    fun updateCliente(cliente: Cliente): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_CLI_NOMBRE, cliente.nombre)
            put(DatabaseHelper.KEY_CLI_TEL, cliente.telefono)
            put(DatabaseHelper.KEY_CLI_DIR, cliente.direccion)
            put(DatabaseHelper.KEY_CLI_EMAIL, cliente.email)
        }
        val result = db.update(DatabaseHelper.TABLE_CLIENTES, values, "${DatabaseHelper.KEY_CLI_ID}=?", arrayOf(cliente.id.toString()))
        db.close()
        return result
    }

    fun deleteCliente(id: Int): Int {
        val db = dbHelper.writableDatabase
        val result = db.delete(DatabaseHelper.TABLE_CLIENTES, "${DatabaseHelper.KEY_CLI_ID}=?", arrayOf(id.toString()))
        db.close()
        return result
    }

    private fun mapCursorToCliente(cursor: Cursor): Cliente {
        return Cliente(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CLI_ID)),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CLI_NOMBRE)),
            telefono = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CLI_TEL)),
            direccion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CLI_DIR)),
            email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CLI_EMAIL))
        )
    }
}
