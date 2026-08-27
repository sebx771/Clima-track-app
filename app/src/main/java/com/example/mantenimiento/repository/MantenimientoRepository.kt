package com.example.mantenimiento.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.mantenimiento.database.DatabaseHelper
import com.example.mantenimiento.models.Mantenimiento

/**
 * Repositorio para gestionar las operaciones de base de datos de Mantenimientos.
 */
class MantenimientoRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    /**
     * Registra un nuevo mantenimiento realizado.
     */
    fun addMantenimiento(mantenimiento: Mantenimiento): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_MNT_EQP_ID, mantenimiento.equipoId)
            put(DatabaseHelper.KEY_MNT_FECHA, mantenimiento.fecha)
            put(DatabaseHelper.KEY_MNT_TIPO, mantenimiento.tipo)
            put(DatabaseHelper.KEY_MNT_DESC, mantenimiento.descripcion)
            put(DatabaseHelper.KEY_MNT_OBS, mantenimiento.observaciones)
            put(DatabaseHelper.KEY_MNT_ESTADO, mantenimiento.estadoFinal)
            put(DatabaseHelper.KEY_MNT_FOTO, mantenimiento.fotoEvidencia)
            put(DatabaseHelper.KEY_MNT_FIRMA, mantenimiento.firmaCliente)
        }
        val id = db.insert(DatabaseHelper.TABLE_MANTENIMIENTOS, null, values)
        db.close()
        return id
    }

    /**
     * Obtiene el historial completo de mantenimientos registrados con el nombre del equipo.
     */
    fun getAllMantenimientos(): List<Mantenimiento> {
        val mntList = mutableListOf<Mantenimiento>()
        val db = dbHelper.readableDatabase
        
        val query = """
            SELECT m.*, e.${DatabaseHelper.KEY_EQP_NOMBRE} 
            FROM ${DatabaseHelper.TABLE_MANTENIMIENTOS} m
            INNER JOIN ${DatabaseHelper.TABLE_EQUIPOS} e ON m.${DatabaseHelper.KEY_MNT_EQP_ID} = e.${DatabaseHelper.KEY_EQP_ID}
            ORDER BY m.${DatabaseHelper.KEY_MNT_FECHA} DESC
        """.trimIndent()

        val cursor = db.rawQuery(query, null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val mnt = mapCursorToMantenimiento(it)
                    mnt.equipoNombre = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_NOMBRE))
                    mntList.add(mnt)
                } while (it.moveToNext())
            }
        }
        db.close()
        return mntList
    }

    /**
     * Obtiene el historial de mantenimientos para un equipo específico con su nombre.
     */
    fun getMantenimientosByEquipo(equipoId: Int): List<Mantenimiento> {
        val mntList = mutableListOf<Mantenimiento>()
        val db = dbHelper.readableDatabase
        
        val query = """
            SELECT m.*, e.${DatabaseHelper.KEY_EQP_NOMBRE} 
            FROM ${DatabaseHelper.TABLE_MANTENIMIENTOS} m
            INNER JOIN ${DatabaseHelper.TABLE_EQUIPOS} e ON m.${DatabaseHelper.KEY_MNT_EQP_ID} = e.${DatabaseHelper.KEY_EQP_ID}
            WHERE m.${DatabaseHelper.KEY_MNT_EQP_ID} = ?
            ORDER BY m.${DatabaseHelper.KEY_MNT_FECHA} DESC
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(equipoId.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val mnt = mapCursorToMantenimiento(it)
                    mnt.equipoNombre = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.KEY_EQP_NOMBRE))
                    mntList.add(mnt)
                } while (it.moveToNext())
            }
        }
        db.close()
        return mntList
    }

    /**
     * Mapea el cursor actual a un objeto Mantenimiento.
     */
    private fun mapCursorToMantenimiento(cursor: Cursor): Mantenimiento {
        return Mantenimiento(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_ID)),
            equipoId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_EQP_ID)),
            fecha = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_FECHA)),
            tipo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_TIPO)),
            descripcion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_DESC)),
            observaciones = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_OBS)),
            estadoFinal = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_ESTADO)),
            fotoEvidencia = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_FOTO)),
            firmaCliente = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_FIRMA))
        )
    }
}
