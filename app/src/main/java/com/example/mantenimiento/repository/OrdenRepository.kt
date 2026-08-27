package com.example.mantenimiento.repository

import android.content.Context
import com.example.mantenimiento.database.DatabaseHelper
import com.example.mantenimiento.models.Orden

class OrdenRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun addOrden(orden: Orden): Long {
        val db = dbHelper.writableDatabase
        val values = android.content.ContentValues().apply {
            put(DatabaseHelper.KEY_ORD_NUMERO, orden.numero)
            put(DatabaseHelper.KEY_ORD_FECHA, orden.fecha)
            put(DatabaseHelper.KEY_ORD_CLIENTE, orden.cliente)
            put(DatabaseHelper.KEY_ORD_DIRECCION, orden.direccion)
            put(DatabaseHelper.KEY_ORD_EQUIPO, orden.equipo)
            put(DatabaseHelper.KEY_ORD_TIPO_SERVICIO, orden.tipoServicio)
            put(DatabaseHelper.KEY_ORD_DESCRIPCION, orden.descripcion)
            put(DatabaseHelper.KEY_ORD_ESTADO, orden.estado)
        }
        val id = db.insert(DatabaseHelper.TABLE_ORDENES, null, values)
        db.close()
        return id
    }

    fun contarOrdenesPorEstado(estado: String): Int {
        val db = dbHelper.readableDatabase
        val query = "SELECT COUNT(*) FROM ${DatabaseHelper.TABLE_ORDENES} WHERE ${DatabaseHelper.KEY_ORD_ESTADO} = ?"
        val cursor = db.rawQuery(query, arrayOf(estado))

        var total = 0
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0)
        }
        cursor.close()
        return total
    }

    fun obtenerOrdenes(): List<Orden> {
        val lista = mutableListOf<Orden>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_ORDENES}", null)

        if (cursor.moveToFirst()) {
            do {
                val orden = Orden(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_ID)),
                    numero = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_NUMERO)),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_FECHA)),
                    cliente = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_CLIENTE)),
                    direccion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_DIRECCION)),
                    equipo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_EQUIPO)),
                    tipoServicio = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_TIPO_SERVICIO)),
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_DESCRIPCION)),
                    estado = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_ESTADO))
                )
                lista.add(orden)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun obtenerOrdenesActivas(): List<Orden> {
        val lista = mutableListOf<Orden>()
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_ORDENES} WHERE ${DatabaseHelper.KEY_ORD_ESTADO} != ?"
        val cursor = db.rawQuery(query, arrayOf("FINALIZADA"))

        if (cursor.moveToFirst()) {
            do {
                val orden = Orden(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_ID)),
                    numero = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_NUMERO)),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_FECHA)),
                    cliente = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_CLIENTE)),
                    direccion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_DIRECCION)),
                    equipo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_EQUIPO)),
                    tipoServicio = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_TIPO_SERVICIO)),
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_DESCRIPCION)),
                    estado = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_ESTADO))
                )
                lista.add(orden)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun obtenerOrdenesFinalizadas(): List<Orden> {
        val lista = mutableListOf<Orden>()
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_ORDENES} WHERE ${DatabaseHelper.KEY_ORD_ESTADO} = ?"
        val cursor = db.rawQuery(query, arrayOf("FINALIZADA"))

        if (cursor.moveToFirst()) {
            do {
                val orden = Orden(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_ID)),
                    numero = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_NUMERO)),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_FECHA)),
                    cliente = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_CLIENTE)),
                    direccion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_DIRECCION)),
                    equipo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_EQUIPO)),
                    tipoServicio = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_TIPO_SERVICIO)),
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_DESCRIPCION)),
                    estado = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_ESTADO))
                )
                lista.add(orden)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun obtenerOrdenesFiltradas(estadoFilter: String?, tipoFilter: String?): List<Orden> {
        val lista = mutableListOf<Orden>()
        val db = dbHelper.readableDatabase

        // Construcción de la consulta con condiciones dinámicas
        var query = "SELECT * FROM ${DatabaseHelper.TABLE_ORDENES} WHERE 1=1"
        val args = mutableListOf<String>()

        if (!estadoFilter.isNullOrEmpty() && estadoFilter != "TODOS") {
            query += " AND ${DatabaseHelper.KEY_ORD_ESTADO} = ?"
            args.add(estadoFilter)
        }

        if (!tipoFilter.isNullOrEmpty() && tipoFilter != "TODOS") {
            query += " AND ${DatabaseHelper.KEY_ORD_TIPO_SERVICIO} = ?"
            args.add(tipoFilter)
        }

        val cursor = db.rawQuery(query, args.toTypedArray())

        if (cursor.moveToFirst()) {
            do {
                val orden = Orden(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_ID)),
                    numero = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_NUMERO)),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_FECHA)),
                    cliente = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_CLIENTE)),
                    direccion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_DIRECCION)),
                    equipo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_EQUIPO)),
                    tipoServicio = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_TIPO_SERVICIO)),
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_DESCRIPCION)),
                    estado = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_ESTADO))
                )
                lista.add(orden)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}