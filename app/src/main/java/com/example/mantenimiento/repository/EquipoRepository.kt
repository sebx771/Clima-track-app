package com.example.mantenimiento.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.mantenimiento.database.DatabaseHelper
import com.example.mantenimiento.models.Equipo

/**
 * Repositorio para gestionar las operaciones de base de datos de los Equipos.
 */
class EquipoRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    /**
     * Inserta un nuevo equipo en la base de datos.
     */
    fun addEquipo(equipo: Equipo): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_EQP_NOMBRE, equipo.nombre)
            put(DatabaseHelper.KEY_EQP_MARCA, equipo.marca)
            put(DatabaseHelper.KEY_EQP_MODELO, equipo.modelo)
            put(DatabaseHelper.KEY_EQP_SERIE, equipo.numeroSerie)
            put(DatabaseHelper.KEY_EQP_UBICACION, equipo.ubicacion)
            put(DatabaseHelper.KEY_EQP_CLIENTE, equipo.cliente)
        }
        val id = db.insert(DatabaseHelper.TABLE_EQUIPOS, null, values)
        db.close()
        return id
    }

    /**
     * Obtiene la lista de todos los equipos registrados.
     */
    fun getAllEquipos(): List<Equipo> {
        val equipoList = mutableListOf<Equipo>()
        val db = dbHelper.readableDatabase
        val selectQuery = "SELECT * FROM ${DatabaseHelper.TABLE_EQUIPOS}"
        val cursor: Cursor = db.rawQuery(selectQuery, null)

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

    /**
     * Obtiene los equipos filtrados por cliente.
     */
    fun getEquiposByCliente(clienteName: String): List<Equipo> {
        val equipoList = mutableListOf<Equipo>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_EQUIPOS,
            null,
            "${DatabaseHelper.KEY_EQP_CLIENTE}=?",
            arrayOf(clienteName),
            null, null, null
        )

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

    /**
     * Busca un equipo específico por su ID.
     */
    fun getEquipoById(id: Int): Equipo? {
        val db = dbHelper.readableDatabase
        var equipo: Equipo? = null
        val cursor = db.query(
            DatabaseHelper.TABLE_EQUIPOS,
            null,
            "${DatabaseHelper.KEY_EQP_ID}=?",
            arrayOf(id.toString()),
            null, null, null
        )

        cursor.use {
            if (it.moveToFirst()) {
                equipo = mapCursorToEquipo(it)
            }
        }
        db.close()
        return equipo
    }

    /**
     * Actualiza la información de un equipo.
     */
    fun updateEquipo(equipo: Equipo): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_EQP_NOMBRE, equipo.nombre)
            put(DatabaseHelper.KEY_EQP_MARCA, equipo.marca)
            put(DatabaseHelper.KEY_EQP_MODELO, equipo.modelo)
            put(DatabaseHelper.KEY_EQP_SERIE, equipo.numeroSerie)
            put(DatabaseHelper.KEY_EQP_UBICACION, equipo.ubicacion)
            put(DatabaseHelper.KEY_EQP_CLIENTE, equipo.cliente)
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

    /**
     * Elimina un equipo de la base de datos por su ID.
     */
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

    /**
     * Mapea el cursor actual a un objeto Equipo.
     */
    private fun mapCursorToEquipo(cursor: Cursor): Equipo {
        return Equipo(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_ID)),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_NOMBRE)),
            marca = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_MARCA)),
            modelo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_MODELO)),
            numeroSerie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_SERIE)),
            ubicacion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_UBICACION)),
            cliente = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_CLIENTE)),
        )
    }
}
