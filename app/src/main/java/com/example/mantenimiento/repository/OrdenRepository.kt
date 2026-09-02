package com.example.mantenimiento.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.mantenimiento.database.DatabaseHelper
import com.example.mantenimiento.models.Orden

class OrdenRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun addOrden(orden: Orden): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_ORD_NUMERO, orden.numero)
            put(DatabaseHelper.KEY_ORD_FECHA, orden.fecha)
            put(DatabaseHelper.KEY_ORD_CLIENTE_ID, orden.clienteId)
            put(DatabaseHelper.KEY_ORD_EQUIPO_ID, orden.equipoId)
            put(DatabaseHelper.KEY_ORD_TIPO_SERVICIO, orden.tipoServicio)
            put(DatabaseHelper.KEY_ORD_DESCRIPCION, orden.descripcion)
            put(DatabaseHelper.KEY_ORD_ESTADO, orden.estado)
            put(DatabaseHelper.KEY_ORD_TEC_ID, orden.tecnicoId)
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
        val query = """
            SELECT o.*, u.${DatabaseHelper.KEY_USR_NOMBRE} as tecnico_nombre, 
                   c.${DatabaseHelper.KEY_CLI_NOMBRE} as cliente_nombre, 
                   e.${DatabaseHelper.KEY_EQP_MARCA} || ' ' || e.${DatabaseHelper.KEY_EQP_MODELO} as equipo_nombre
            FROM ${DatabaseHelper.TABLE_ORDENES} o
            LEFT JOIN ${DatabaseHelper.TABLE_USUARIOS} u ON o.${DatabaseHelper.KEY_ORD_TEC_ID} = u.${DatabaseHelper.KEY_USR_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_CLIENTES} c ON o.${DatabaseHelper.KEY_ORD_CLIENTE_ID} = c.${DatabaseHelper.KEY_CLI_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_EQUIPOS} e ON o.${DatabaseHelper.KEY_ORD_EQUIPO_ID} = e.${DatabaseHelper.KEY_EQP_ID}
        """.trimIndent()
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                lista.add(mapCursorToOrden(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    private fun mapCursorToOrden(cursor: Cursor): Orden {
        val orden = Orden(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_ID)),
            numero = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_NUMERO)),
            fecha = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_FECHA)),
            clienteId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_CLIENTE_ID)),
            equipoId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_EQUIPO_ID)),
            tipoServicio = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_TIPO_SERVICIO)),
            descripcion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_DESCRIPCION)),
            estado = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_ESTADO)),
            tecnicoId = if (cursor.isNull(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_TEC_ID))) null else cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORD_TEC_ID))
        )
        
        val idxTecnico = cursor.getColumnIndex("tecnico_nombre")
        if (idxTecnico != -1) orden.tecnicoNombre = cursor.getString(idxTecnico)
        
        val idxCliente = cursor.getColumnIndex("cliente_nombre")
        if (idxCliente != -1) orden.clienteNombre = cursor.getString(idxCliente)
        
        val idxEquipo = cursor.getColumnIndex("equipo_nombre")
        if (idxEquipo != -1) orden.equipoNombre = cursor.getString(idxEquipo)
        
        return orden
    }

    fun obtenerOrdenesActivas(): List<Orden> {
        val lista = mutableListOf<Orden>()
        val db = dbHelper.readableDatabase
        val query = """
            SELECT o.*, u.${DatabaseHelper.KEY_USR_NOMBRE} as tecnico_nombre, 
                   c.${DatabaseHelper.KEY_CLI_NOMBRE} as cliente_nombre, 
                   e.${DatabaseHelper.KEY_EQP_MARCA} || ' ' || e.${DatabaseHelper.KEY_EQP_MODELO} as equipo_nombre
            FROM ${DatabaseHelper.TABLE_ORDENES} o
            LEFT JOIN ${DatabaseHelper.TABLE_USUARIOS} u ON o.${DatabaseHelper.KEY_ORD_TEC_ID} = u.${DatabaseHelper.KEY_USR_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_CLIENTES} c ON o.${DatabaseHelper.KEY_ORD_CLIENTE_ID} = c.${DatabaseHelper.KEY_CLI_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_EQUIPOS} e ON o.${DatabaseHelper.KEY_ORD_EQUIPO_ID} = e.${DatabaseHelper.KEY_EQP_ID}
            WHERE o.${DatabaseHelper.KEY_ORD_ESTADO} != ?
        """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf("FINALIZADA"))

        if (cursor.moveToFirst()) {
            do {
                lista.add(mapCursorToOrden(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun obtenerOrdenesAsignadas(tecnicoId: Int): List<Orden> {
        val lista = mutableListOf<Orden>()
        val db = dbHelper.readableDatabase
        val query = """
            SELECT o.*, u.${DatabaseHelper.KEY_USR_NOMBRE} as tecnico_nombre, 
                   c.${DatabaseHelper.KEY_CLI_NOMBRE} as cliente_nombre, 
                   e.${DatabaseHelper.KEY_EQP_MARCA} || ' ' || e.${DatabaseHelper.KEY_EQP_MODELO} as equipo_nombre
            FROM ${DatabaseHelper.TABLE_ORDENES} o
            LEFT JOIN ${DatabaseHelper.TABLE_USUARIOS} u ON o.${DatabaseHelper.KEY_ORD_TEC_ID} = u.${DatabaseHelper.KEY_USR_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_CLIENTES} c ON o.${DatabaseHelper.KEY_ORD_CLIENTE_ID} = c.${DatabaseHelper.KEY_CLI_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_EQUIPOS} e ON o.${DatabaseHelper.KEY_ORD_EQUIPO_ID} = e.${DatabaseHelper.KEY_EQP_ID}
            WHERE o.${DatabaseHelper.KEY_ORD_TEC_ID} = ?
        """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(tecnicoId.toString()))

        if (cursor.moveToFirst()) {
            do {
                lista.add(mapCursorToOrden(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun asignarTecnico(ordenId: Int, tecnicoId: Int): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_ORD_TEC_ID, tecnicoId)
            put(DatabaseHelper.KEY_ORD_ESTADO, "PENDIENTE")
        }
        val result = db.update(DatabaseHelper.TABLE_ORDENES, values, "${DatabaseHelper.KEY_ORD_ID}=?", arrayOf(ordenId.toString()))
        db.close()
        return result
    }

    fun actualizarEstadoOrden(ordenId: Int, nuevoEstado: String): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.KEY_ORD_ESTADO, nuevoEstado)
        }
        val result = db.update(DatabaseHelper.TABLE_ORDENES, values, "${DatabaseHelper.KEY_ORD_ID}=?", arrayOf(ordenId.toString()))
        db.close()
        return result
    }

    fun obtenerOrdenesFiltradas(estadoFilter: String?, tipoFilter: String?): List<Orden> {
        val lista = mutableListOf<Orden>()
        val db = dbHelper.readableDatabase

        var query = """
            SELECT o.*, u.${DatabaseHelper.KEY_USR_NOMBRE} as tecnico_nombre, 
                   c.${DatabaseHelper.KEY_CLI_NOMBRE} as cliente_nombre, 
                   e.${DatabaseHelper.KEY_EQP_MARCA} || ' ' || e.${DatabaseHelper.KEY_EQP_MODELO} as equipo_nombre
            FROM ${DatabaseHelper.TABLE_ORDENES} o
            LEFT JOIN ${DatabaseHelper.TABLE_USUARIOS} u ON o.${DatabaseHelper.KEY_ORD_TEC_ID} = u.${DatabaseHelper.KEY_USR_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_CLIENTES} c ON o.${DatabaseHelper.KEY_ORD_CLIENTE_ID} = c.${DatabaseHelper.KEY_CLI_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_EQUIPOS} e ON o.${DatabaseHelper.KEY_ORD_EQUIPO_ID} = e.${DatabaseHelper.KEY_EQP_ID}
            WHERE 1=1
        """.trimIndent()
        val args = mutableListOf<String>()

        if (!estadoFilter.isNullOrEmpty() && estadoFilter != "TODOS") {
            query += " AND o.${DatabaseHelper.KEY_ORD_ESTADO} = ?"
            args.add(estadoFilter)
        }

        if (!tipoFilter.isNullOrEmpty() && tipoFilter != "TODOS") {
            query += " AND o.${DatabaseHelper.KEY_ORD_TIPO_SERVICIO} = ?"
            args.add(tipoFilter)
        }

        val cursor = db.rawQuery(query, args.toTypedArray())

        if (cursor.moveToFirst()) {
            do {
                lista.add(mapCursorToOrden(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun obtenerOrdenesFinalizadas(): List<Orden> {
        val lista = mutableListOf<Orden>()
        val db = dbHelper.readableDatabase
        val query = """
            SELECT o.*, u.${DatabaseHelper.KEY_USR_NOMBRE} as tecnico_nombre, 
                   c.${DatabaseHelper.KEY_CLI_NOMBRE} as cliente_nombre, 
                   e.${DatabaseHelper.KEY_EQP_MARCA} || ' ' || e.${DatabaseHelper.KEY_EQP_MODELO} as equipo_nombre
            FROM ${DatabaseHelper.TABLE_ORDENES} o
            LEFT JOIN ${DatabaseHelper.TABLE_USUARIOS} u ON o.${DatabaseHelper.KEY_ORD_TEC_ID} = u.${DatabaseHelper.KEY_USR_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_CLIENTES} c ON o.${DatabaseHelper.KEY_ORD_CLIENTE_ID} = c.${DatabaseHelper.KEY_CLI_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_EQUIPOS} e ON o.${DatabaseHelper.KEY_ORD_EQUIPO_ID} = e.${DatabaseHelper.KEY_EQP_ID}
            WHERE o.${DatabaseHelper.KEY_ORD_ESTADO} = ?
        """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf("FINALIZADA"))

        if (cursor.moveToFirst()) {
            do {
                lista.add(mapCursorToOrden(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun obtenerOrdenesFinalizadasPorCliente(clienteId: Int): List<Orden> {
        val lista = mutableListOf<Orden>()
        val db = dbHelper.readableDatabase
        val query = """
            SELECT o.*, u.${DatabaseHelper.KEY_USR_NOMBRE} as tecnico_nombre, 
                   c.${DatabaseHelper.KEY_CLI_NOMBRE} as cliente_nombre, 
                   e.${DatabaseHelper.KEY_EQP_MARCA} || ' ' || e.${DatabaseHelper.KEY_EQP_MODELO} as equipo_nombre
            FROM ${DatabaseHelper.TABLE_ORDENES} o
            LEFT JOIN ${DatabaseHelper.TABLE_USUARIOS} u ON o.${DatabaseHelper.KEY_ORD_TEC_ID} = u.${DatabaseHelper.KEY_USR_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_CLIENTES} c ON o.${DatabaseHelper.KEY_ORD_CLIENTE_ID} = c.${DatabaseHelper.KEY_CLI_ID}
            LEFT JOIN ${DatabaseHelper.TABLE_EQUIPOS} e ON o.${DatabaseHelper.KEY_ORD_EQUIPO_ID} = e.${DatabaseHelper.KEY_EQP_ID}
            WHERE o.${DatabaseHelper.KEY_ORD_ESTADO} = ? AND o.${DatabaseHelper.KEY_ORD_CLIENTE_ID} = ?
        """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf("FINALIZADA", clienteId.toString()))

        if (cursor.moveToFirst()) {
            do {
                lista.add(mapCursorToOrden(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}
