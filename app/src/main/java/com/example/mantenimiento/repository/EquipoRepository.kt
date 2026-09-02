package com.example.mantenimiento.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.mantenimiento.database.DatabaseHelper
import com.example.mantenimiento.models.Equipo

class EquipoRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addEquipo(equipo: Equipo): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_EQP_CODIGO, equipo.codigo)
            put(DatabaseHelper.KEY_EQP_TIPO, equipo.tipo)
            put(DatabaseHelper.KEY_EQP_MARCA, equipo.marca)
            put(DatabaseHelper.KEY_EQP_MODELO, equipo.modelo)
            put(DatabaseHelper.KEY_EQP_SERIAL, equipo.serial)
            put(DatabaseHelper.KEY_EQP_CAPACIDAD, equipo.capacidad)
            put(DatabaseHelper.KEY_EQP_UBICACION, equipo.ubicacion)
            put(DatabaseHelper.KEY_EQP_CLIENTE_ID, equipo.clienteId)
            put(DatabaseHelper.KEY_EQP_ESTADO, equipo.estado)
        }
        val id = db.insert(DatabaseHelper.TABLE_EQUIPOS, null, values)
        db.close()
        return id
    }

    fun getAllEquipos(): List<Equipo> {
        val equipoList = mutableListOf<Equipo>()
        val db = dbHelper.readableDatabase
        val query = """
            SELECT e.*, c.${DatabaseHelper.KEY_CLI_NOMBRE} as cliente_nombre
            FROM ${DatabaseHelper.TABLE_EQUIPOS} e
            LEFT JOIN ${DatabaseHelper.TABLE_CLIENTES} c ON e.${DatabaseHelper.KEY_EQP_CLIENTE_ID} = c.${DatabaseHelper.KEY_CLI_ID}
        """.trimIndent()
        val cursor: Cursor = db.rawQuery(query, null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    equipoList.add(mapCursorToEquipo(it))
                } while (it.moveToNext())
            }
        }
        db.close()
        return equipoList
    }

    fun searchEquipos(queryText: String): List<Equipo> {
        val equipoList = mutableListOf<Equipo>()
        val db = dbHelper.readableDatabase
        val query = """
            SELECT e.*, c.${DatabaseHelper.KEY_CLI_NOMBRE} as cliente_nombre
            FROM ${DatabaseHelper.TABLE_EQUIPOS} e
            LEFT JOIN ${DatabaseHelper.TABLE_CLIENTES} c ON e.${DatabaseHelper.KEY_EQP_CLIENTE_ID} = c.${DatabaseHelper.KEY_CLI_ID}
            WHERE e.${DatabaseHelper.KEY_EQP_CODIGO} LIKE ? 
               OR e.${DatabaseHelper.KEY_EQP_MARCA} LIKE ? 
               OR c.${DatabaseHelper.KEY_CLI_NOMBRE} LIKE ?
        """.trimIndent()
        val searchText = "%$queryText%"
        val cursor = db.rawQuery(query, arrayOf(searchText, searchText, searchText))

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    equipoList.add(mapCursorToEquipo(it))
                } while (it.moveToNext())
            }
        }
        db.close()
        return equipoList
    }

    fun getEquiposByCliente(clienteId: Int): List<Equipo> {
        val equipoList = mutableListOf<Equipo>()
        val db = dbHelper.readableDatabase
        val query = """
            SELECT e.*, c.${DatabaseHelper.KEY_CLI_NOMBRE} as cliente_nombre
            FROM ${DatabaseHelper.TABLE_EQUIPOS} e
            LEFT JOIN ${DatabaseHelper.TABLE_CLIENTES} c ON e.${DatabaseHelper.KEY_EQP_CLIENTE_ID} = c.${DatabaseHelper.KEY_CLI_ID}
            WHERE e.${DatabaseHelper.KEY_EQP_CLIENTE_ID} = ?
        """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(clienteId.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    equipoList.add(mapCursorToEquipo(it))
                } while (it.moveToNext())
            }
        }
        db.close()
        return equipoList
    }

    fun getEquipoById(id: Int): Equipo? {
        val db = dbHelper.readableDatabase
        var equipo: Equipo? = null
        val query = """
            SELECT e.*, c.${DatabaseHelper.KEY_CLI_NOMBRE} as cliente_nombre
            FROM ${DatabaseHelper.TABLE_EQUIPOS} e
            LEFT JOIN ${DatabaseHelper.TABLE_CLIENTES} c ON e.${DatabaseHelper.KEY_EQP_CLIENTE_ID} = c.${DatabaseHelper.KEY_CLI_ID}
            WHERE e.${DatabaseHelper.KEY_EQP_ID} = ?
        """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(id.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                equipo = mapCursorToEquipo(it)
            }
        }
        db.close()
        return equipo
    }

    fun updateEquipo(equipo: Equipo): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_EQP_CODIGO, equipo.codigo)
            put(DatabaseHelper.KEY_EQP_TIPO, equipo.tipo)
            put(DatabaseHelper.KEY_EQP_MARCA, equipo.marca)
            put(DatabaseHelper.KEY_EQP_MODELO, equipo.modelo)
            put(DatabaseHelper.KEY_EQP_SERIAL, equipo.serial)
            put(DatabaseHelper.KEY_EQP_CAPACIDAD, equipo.capacidad)
            put(DatabaseHelper.KEY_EQP_UBICACION, equipo.ubicacion)
            put(DatabaseHelper.KEY_EQP_CLIENTE_ID, equipo.clienteId)
            put(DatabaseHelper.KEY_EQP_ESTADO, equipo.estado)
        }
        val result = db.update(
            DatabaseHelper.TABLE_EQUIPOS,
            values,
            "${DatabaseHelper.KEY_EQP_ID}=?",
            arrayOf(equipo.id.toString())
        )
        db.close()
        return result
    }

    fun deleteEquipo(id: Int): Int {
        val db = dbHelper.writableDatabase
        val result = db.delete(
            DatabaseHelper.TABLE_EQUIPOS,
            "${DatabaseHelper.KEY_EQP_ID}=?",
            arrayOf(id.toString())
        )
        db.close()
        return result
    }

    private fun mapCursorToEquipo(cursor: Cursor): Equipo {
        val equipo = Equipo(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_ID)),
            codigo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_CODIGO)),
            tipo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_TIPO)),
            marca = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_MARCA)),
            modelo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_MODELO)),
            serial = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_SERIAL)),
            capacidad = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_CAPACIDAD)),
            ubicacion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_UBICACION)),
            clienteId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_CLIENTE_ID)),
            estado = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_ESTADO))
        )
        val idxCliente = cursor.getColumnIndex("cliente_nombre")
        if (idxCliente != -1) {
            equipo.clienteNombre = cursor.getString(idxCliente)
        }
        return equipo
    }
}
