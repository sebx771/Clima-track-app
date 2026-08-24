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
            cantidadDisponible = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_REP_STOCK)),
        )
    }
}
