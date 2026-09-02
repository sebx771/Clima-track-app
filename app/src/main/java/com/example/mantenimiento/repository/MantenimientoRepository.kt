package com.example.mantenimiento.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.mantenimiento.database.DatabaseHelper
import com.example.mantenimiento.models.Mantenimiento

class MantenimientoRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addMantenimiento(mantenimiento: Mantenimiento): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_MNT_ORD_ID, mantenimiento.ordenId)
            put(DatabaseHelper.KEY_MNT_FECHA, mantenimiento.fecha)
            put(DatabaseHelper.KEY_MNT_DIAG, mantenimiento.diagnostico)
            put(DatabaseHelper.KEY_MNT_TRABAJO, mantenimiento.trabajoRealizado)
            put(DatabaseHelper.KEY_MNT_OBS, mantenimiento.observaciones)
            put(DatabaseHelper.KEY_MNT_RECOM, mantenimiento.recomendaciones)
        }
        val id = db.insert(DatabaseHelper.TABLE_MANTENIMIENTOS, null, values)
        db.close()
        return id
    }

    fun getAllMantenimientos(): List<Mantenimiento> {
        val mntList = mutableListOf<Mantenimiento>()
        val db = dbHelper.readableDatabase
        
        val query = """
            SELECT m.*, o.${DatabaseHelper.KEY_ORD_NUMERO} as numero_orden, 
                   e.${DatabaseHelper.KEY_EQP_MARCA} || ' ' || e.${DatabaseHelper.KEY_EQP_MODELO} as equipo_nombre,
                   evi.${DatabaseHelper.KEY_EVI_RUTA} as foto_ruta,
                   apr.${DatabaseHelper.KEY_APR_RUTA_FIRMA} as firma_ruta
            FROM ${DatabaseHelper.TABLE_MANTENIMIENTOS} m
            INNER JOIN ${DatabaseHelper.TABLE_ORDENES} o ON m.${DatabaseHelper.KEY_MNT_ORD_ID} = o.${DatabaseHelper.KEY_ORD_ID}
            INNER JOIN ${DatabaseHelper.TABLE_EQUIPOS} e ON o.${DatabaseHelper.KEY_ORD_EQUIPO_ID} = e.${DatabaseHelper.KEY_EQP_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_EVIDENCIAS} evi ON o.${DatabaseHelper.KEY_ORD_ID} = evi.${DatabaseHelper.KEY_EVI_ORD_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_APROBACIONES} apr ON o.${DatabaseHelper.KEY_ORD_ID} = apr.${DatabaseHelper.KEY_APR_ORD_ID}
            ORDER BY m.${DatabaseHelper.KEY_MNT_FECHA} DESC
        """.trimIndent()

        val cursor = db.rawQuery(query, null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    mntList.add(mapCursorToMantenimiento(it))
                } while (it.moveToNext())
            }
        }
        db.close()
        return mntList
    }

    fun getMantenimientosByCliente(clienteId: Int): List<Mantenimiento> {
        val mntList = mutableListOf<Mantenimiento>()
        val db = dbHelper.readableDatabase
        
        val query = """
            SELECT m.*, o.${DatabaseHelper.KEY_ORD_NUMERO} as numero_orden, 
                   e.${DatabaseHelper.KEY_EQP_MARCA} || ' ' || e.${DatabaseHelper.KEY_EQP_MODELO} as equipo_nombre,
                   evi.${DatabaseHelper.KEY_EVI_RUTA} as foto_ruta,
                   apr.${DatabaseHelper.KEY_APR_RUTA_FIRMA} as firma_ruta
            FROM ${DatabaseHelper.TABLE_MANTENIMIENTOS} m
            INNER JOIN ${DatabaseHelper.TABLE_ORDENES} o ON m.${DatabaseHelper.KEY_MNT_ORD_ID} = o.${DatabaseHelper.KEY_ORD_ID}
            INNER JOIN ${DatabaseHelper.TABLE_EQUIPOS} e ON o.${DatabaseHelper.KEY_ORD_EQUIPO_ID} = e.${DatabaseHelper.KEY_EQP_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_EVIDENCIAS} evi ON o.${DatabaseHelper.KEY_ORD_ID} = evi.${DatabaseHelper.KEY_EVI_ORD_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_APROBACIONES} apr ON o.${DatabaseHelper.KEY_ORD_ID} = apr.${DatabaseHelper.KEY_APR_ORD_ID}
            WHERE o.${DatabaseHelper.KEY_ORD_CLIENTE_ID} = ?
            ORDER BY m.${DatabaseHelper.KEY_MNT_FECHA} DESC
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(clienteId.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    mntList.add(mapCursorToMantenimiento(it))
                } while (it.moveToNext())
            }
        }
        db.close()
        return mntList
    }

    fun addEvidencia(ordenId: Int, ruta: String, fecha: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_EVI_ORD_ID, ordenId)
            put(DatabaseHelper.KEY_EVI_RUTA, ruta)
            put(DatabaseHelper.KEY_EVI_FECHA, fecha)
        }
        val id = db.insert(DatabaseHelper.TABLE_EVIDENCIAS, null, values)
        db.close()
        return id
    }

    fun addUbicacion(ordenId: Int, lat: Double, lon: Double, fecha: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_UBI_ORD_ID, ordenId)
            put(DatabaseHelper.KEY_UBI_LAT, lat)
            put(DatabaseHelper.KEY_UBI_LON, lon)
            put(DatabaseHelper.KEY_UBI_FECHA, fecha)
        }
        val id = db.insert(DatabaseHelper.TABLE_UBICACIONES, null, values)
        db.close()
        return id
    }

    fun addAprobacion(ordenId: Int, cliente: String, rutaFirma: String?, aceptado: Boolean, fecha: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_APR_ORD_ID, ordenId)
            put(DatabaseHelper.KEY_APR_CLIENTE, cliente)
            put(DatabaseHelper.KEY_APR_RUTA_FIRMA, rutaFirma)
            put(DatabaseHelper.KEY_APR_ACEPTADO, if (aceptado) 1 else 0)
            put(DatabaseHelper.KEY_APR_FECHA, fecha)
        }
        val id = db.insert(DatabaseHelper.TABLE_APROBACIONES, null, values)
        db.close()
        return id
    }

    fun addDetalleRepuesto(mantenimientoId: Int, repuestoId: Int, cantidad: Int): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_DR_MNT_ID, mantenimientoId)
            put(DatabaseHelper.KEY_DR_REP_ID, repuestoId)
            put(DatabaseHelper.KEY_DR_CANT, cantidad)
        }
        val id = db.insert(DatabaseHelper.TABLE_DETALLE_REPUESTOS, null, values)
        db.close()
        return id
    }

    private fun mapCursorToMantenimiento(cursor: Cursor): Mantenimiento {
        val mnt = Mantenimiento(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_ID)),
            ordenId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_ORD_ID)),
            fecha = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_FECHA)),
            diagnostico = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_DIAG)),
            trabajoRealizado = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_TRABAJO)),
            observaciones = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_OBS)),
            recomendaciones = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MNT_RECOM))
        )
        cursor.getColumnIndex("equipo_nombre").let { if (it != -1) mnt.equipoNombre = cursor.getString(it) }
        cursor.getColumnIndex("numero_orden").let { if (it != -1) mnt.numeroOrden = cursor.getString(it) }
        cursor.getColumnIndex("foto_ruta").let { if (it != -1) mnt.fotoEvidencia = cursor.getString(it) }
        cursor.getColumnIndex("firma_ruta").let { if (it != -1) mnt.firmaCliente = cursor.getString(it) }
        
        return mnt
    }
}
