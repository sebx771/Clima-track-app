package com.example.mantenimiento.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.mantenimiento.database.DatabaseHelper
import com.example.mantenimiento.models.Repuesto

/**
 * Repositorio para gestionar el catálogo y stock de Repuestos e Insumos.
 */
class RepuestoRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    /**
     * Obtiene la lista completa de repuestos disponibles.
     */
    fun getAllRepuestos(): List<Repuesto> {
        val repuestoList = mutableListOf<Repuesto>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(DatabaseHelper.TABLE_REPUESTOS, null, null, null, null, null, null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    repuestoList.add(mapCursorToRepuesto(it))
                } while (it.moveToNext())
            }
        }
        db.close()
        return repuestoList
    }

    /**
     * Inserta un nuevo repuesto.
     */
    fun addRepuesto(repuesto: Repuesto): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_REP_NOMBRE, repuesto.nombre)
            put(DatabaseHelper.KEY_REP_CODIGO, repuesto.codigo)
            put(DatabaseHelper.KEY_REP_UNIDAD, repuesto.unidad)
            put(DatabaseHelper.KEY_REP_STOCK, repuesto.cantidadDisponible)
        }
        val id = db.insert(DatabaseHelper.TABLE_REPUESTOS, null, values)
        db.close()
        return id
    }

    /**
     * Busca un repuesto por ID.
     */
    fun getRepuestoById(id: Int): Repuesto? {
        val db = dbHelper.readableDatabase
        var repuesto: Repuesto? = null
        val cursor = db.query(
            DatabaseHelper.TABLE_REPUESTOS,
            null,
            "${DatabaseHelper.KEY_REP_ID}=?",
            arrayOf(id.toString()),
            null, null, null
        )

        cursor.use {
            if (it.moveToFirst()) {
                repuesto = mapCursorToRepuesto(it)
            }
        }
        db.close()
        return repuesto
    }

    /**
     * Actualiza la información de un repuesto.
     */
    fun updateRepuesto(repuesto: Repuesto): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_REP_NOMBRE, repuesto.nombre)
            put(DatabaseHelper.KEY_REP_CODIGO, repuesto.codigo)
            put(DatabaseHelper.KEY_REP_UNIDAD, repuesto.unidad)
            put(DatabaseHelper.KEY_REP_STOCK, repuesto.cantidadDisponible)
        }
        val result = db.update(
            DatabaseHelper.TABLE_REPUESTOS,
            values,
            "${DatabaseHelper.KEY_REP_ID}=?",
            arrayOf(repuesto.id.toString())
        )
        db.close()
        return result
    }

    /**
     * Elimina un repuesto.
     */
    fun deleteRepuesto(id: Int): Int {
        val db = dbHelper.writableDatabase
        val result = db.delete(
            DatabaseHelper.TABLE_REPUESTOS,
            "${DatabaseHelper.KEY_REP_ID}=?",
            arrayOf(id.toString())
        )
        db.close()
        return result
    }

    /**
     * Actualiza el stock de un repuesto específico.
     */
    fun updateStock(repuestoId: Int, nuevaCantidad: Int): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_REP_STOCK, nuevaCantidad)
        }
        val result = db.update(
            DatabaseHelper.TABLE_REPUESTOS,
            values,
            "${DatabaseHelper.KEY_REP_ID}=?",
            arrayOf(repuestoId.toString())
        )
        db.close()
        return result
    }

    /**
     * Mapea el cursor actual a un objeto Repuesto.
     */
    private fun mapCursorToRepuesto(cursor: Cursor): Repuesto {
        return Repuesto(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_REP_ID)),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_REP_NOMBRE)),
            codigo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_REP_CODIGO)),
            unidad = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_REP_UNIDAD)),
            cantidadDisponible = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_REP_STOCK)),
        )
    }
}
