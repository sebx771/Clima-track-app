package com.example.mantenimiento.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        // nombre y version de la db
        private const val DATABASE_NAME = "climatrack.db"
        private const val DATABASE_VERSION = 10

        // Nombres de Tablas
        const val TABLE_USUARIOS = "usuarios"
        const val TABLE_CLIENTES = "clientes"
        const val TABLE_EQUIPOS = "equipos"
        const val TABLE_ORDENES = "ordenes"
        const val TABLE_MANTENIMIENTOS = "mantenimientos"
        const val TABLE_REPUESTOS = "repuestos"
        const val TABLE_DETALLE_REPUESTOS = "detalle_repuestos"
        const val TABLE_EVIDENCIAS = "evidencias"
        const val TABLE_APROBACIONES = "aprobaciones"
        const val TABLE_UBICACIONES = "ubicaciones"

        // Columnas Tabla Usuarios
        const val KEY_USR_ID = "id"
        const val KEY_USR_USUARIO = "usuario"
        const val KEY_USR_PASSWORD = "password"
        const val KEY_USR_NOMBRE = "nombre"
        const val KEY_USR_ROL = "rol"
        const val KEY_USR_EMAIL = "email"

        // Columnas Tabla Clientes
        const val KEY_CLI_ID = "id"
        const val KEY_CLI_NOMBRE = "nombre"
        const val KEY_CLI_TEL = "telefono"
        const val KEY_CLI_DIR = "direccion"
        const val KEY_CLI_EMAIL = "email"

        // Columnas Tabla Equipos
        const val KEY_EQP_ID = "id"
        const val KEY_EQP_CODIGO = "codigo"
        const val KEY_EQP_TIPO = "tipo"
        const val KEY_EQP_MARCA = "marca"
        const val KEY_EQP_MODELO = "modelo"
        const val KEY_EQP_SERIAL = "serial"
        const val KEY_EQP_CAPACIDAD = "capacidad"
        const val KEY_EQP_UBICACION = "ubicacion"
        const val KEY_EQP_CLIENTE_ID = "cliente_id"
        const val KEY_EQP_ESTADO = "estado"

        // Columnas Tabla Órdenes
        const val KEY_ORD_ID = "id"
        const val KEY_ORD_NUMERO = "numero"
        const val KEY_ORD_FECHA = "fecha"
        const val KEY_ORD_CLIENTE_ID = "cliente_id"
        const val KEY_ORD_EQUIPO_ID = "equipo_id"
        const val KEY_ORD_TEC_ID = "tecnico_id"
        const val KEY_ORD_TIPO_SERVICIO = "tipo_servicio"
        const val KEY_ORD_DESCRIPCION = "descripcion"
        const val KEY_ORD_ESTADO = "estado"

        // Columnas Tabla Mantenimientos
        const val KEY_MNT_ID = "id"
        const val KEY_MNT_ORD_ID = "orden_id"
        const val KEY_MNT_FECHA = "fecha"
        const val KEY_MNT_DIAG = "diagnostico"
        const val KEY_MNT_TRABAJO = "trabajo_realizado"
        const val KEY_MNT_OBS = "observaciones"
        const val KEY_MNT_RECOM = "recomendaciones"

        // Columnas Tabla Repuestos
        const val KEY_REP_ID = "id"
        const val KEY_REP_NOMBRE = "nombre"
        const val KEY_REP_CODIGO = "codigo"
        const val KEY_REP_UNIDAD = "unidad"
        const val KEY_REP_STOCK = "cantidad_disponible"

        // Columnas Tabla Detalle Repuestos
        const val KEY_DR_ID = "id"
        const val KEY_DR_MNT_ID = "mantenimiento_id"
        const val KEY_DR_REP_ID = "repuesto_id"
        const val KEY_DR_CANT = "cantidad"

        // Columnas Tabla Evidencias
        const val KEY_EVI_ID = "id"
        const val KEY_EVI_ORD_ID = "orden_id"
        const val KEY_EVI_RUTA = "ruta_foto"
        const val KEY_EVI_FECHA = "fecha"

        // Columnas Tabla Aprobaciones
        const val KEY_APR_ID = "id"
        const val KEY_APR_ORD_ID = "orden_id"
        const val KEY_APR_CLIENTE = "cliente"
        const val KEY_APR_RUTA_FIRMA = "ruta_firma"
        const val KEY_APR_ACEPTADO = "aceptado"
        const val KEY_APR_FECHA = "fecha"

        // Columnas Tabla Ubicaciones
        const val KEY_UBI_ID = "id"
        const val KEY_UBI_ORD_ID = "orden_id"
        const val KEY_UBI_LAT = "latitud"
        const val KEY_UBI_LON = "longitud"
        const val KEY_UBI_FECHA = "fecha"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // 1. Tabla Usuarios
        db.execSQL("""
            CREATE TABLE $TABLE_USUARIOS (
                $KEY_USR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_USR_USUARIO TEXT,
                $KEY_USR_PASSWORD TEXT,
                $KEY_USR_NOMBRE TEXT,
                $KEY_USR_ROL TEXT,
                $KEY_USR_EMAIL TEXT
            )
        """)

        // 2. Tabla Clientes
        db.execSQL("""
            CREATE TABLE $TABLE_CLIENTES (
                $KEY_CLI_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_CLI_NOMBRE TEXT,
                $KEY_CLI_TEL TEXT,
                $KEY_CLI_DIR TEXT,
                $KEY_CLI_EMAIL TEXT
            )
        """)

        // 3. Tabla Equipos
        db.execSQL("""
            CREATE TABLE $TABLE_EQUIPOS (
                $KEY_EQP_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_EQP_CODIGO TEXT,
                $KEY_EQP_TIPO TEXT,
                $KEY_EQP_MARCA TEXT,
                $KEY_EQP_MODELO TEXT,
                $KEY_EQP_SERIAL TEXT,
                $KEY_EQP_CAPACIDAD TEXT,
                $KEY_EQP_UBICACION TEXT,
                $KEY_EQP_CLIENTE_ID INTEGER,
                $KEY_EQP_ESTADO TEXT,
                FOREIGN KEY($KEY_EQP_CLIENTE_ID) REFERENCES $TABLE_CLIENTES($KEY_CLI_ID)
            )
        """)

        // 4. Tabla Órdenes
        db.execSQL("""
            CREATE TABLE $TABLE_ORDENES (
                $KEY_ORD_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_ORD_NUMERO TEXT,
                $KEY_ORD_FECHA TEXT,
                $KEY_ORD_CLIENTE_ID INTEGER,
                $KEY_ORD_EQUIPO_ID INTEGER,
                $KEY_ORD_TEC_ID INTEGER,
                $KEY_ORD_TIPO_SERVICIO TEXT,
                $KEY_ORD_DESCRIPCION TEXT,
                $KEY_ORD_ESTADO TEXT,
                FOREIGN KEY($KEY_ORD_CLIENTE_ID) REFERENCES $TABLE_CLIENTES($KEY_CLI_ID),
                FOREIGN KEY($KEY_ORD_EQUIPO_ID) REFERENCES $TABLE_EQUIPOS($KEY_EQP_ID),
                FOREIGN KEY($KEY_ORD_TEC_ID) REFERENCES $TABLE_USUARIOS($KEY_USR_ID)
            )
        """)

        // 5. Tabla Mantenimientos
        db.execSQL("""
            CREATE TABLE $TABLE_MANTENIMIENTOS (
                $KEY_MNT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_MNT_ORD_ID INTEGER,
                $KEY_MNT_FECHA TEXT,
                $KEY_MNT_DIAG TEXT,
                $KEY_MNT_TRABAJO TEXT,
                $KEY_MNT_OBS TEXT,
                $KEY_MNT_RECOM TEXT,
                FOREIGN KEY($KEY_MNT_ORD_ID) REFERENCES $TABLE_ORDENES($KEY_ORD_ID)
            )
        """)

        // 6. Tabla Repuestos
        db.execSQL("""
            CREATE TABLE $TABLE_REPUESTOS (
                $KEY_REP_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_REP_NOMBRE TEXT,
                $KEY_REP_CODIGO TEXT,
                $KEY_REP_UNIDAD TEXT,
                $KEY_REP_STOCK INTEGER
            )
        """)

        // 7. Tabla Detalle Repuestos
        db.execSQL("""
            CREATE TABLE $TABLE_DETALLE_REPUESTOS (
                $KEY_DR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_DR_MNT_ID INTEGER,
                $KEY_DR_REP_ID INTEGER,
                $KEY_DR_CANT INTEGER,
                FOREIGN KEY($KEY_DR_MNT_ID) REFERENCES $TABLE_MANTENIMIENTOS($KEY_MNT_ID),
                FOREIGN KEY($KEY_DR_REP_ID) REFERENCES $TABLE_REPUESTOS($KEY_REP_ID)
            )
        """)

        // 8. Tabla Evidencias
        db.execSQL("""
            CREATE TABLE $TABLE_EVIDENCIAS (
                $KEY_EVI_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_EVI_ORD_ID INTEGER,
                $KEY_EVI_RUTA TEXT,
                $KEY_EVI_FECHA TEXT,
                FOREIGN KEY($KEY_EVI_ORD_ID) REFERENCES $TABLE_ORDENES($KEY_ORD_ID)
            )
        """)

        // 9. Tabla Aprobaciones
        db.execSQL("""
            CREATE TABLE $TABLE_APROBACIONES (
                $KEY_APR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_APR_ORD_ID INTEGER,
                $KEY_APR_CLIENTE TEXT,
                $KEY_APR_RUTA_FIRMA TEXT,
                $KEY_APR_ACEPTADO INTEGER,
                $KEY_APR_FECHA TEXT,
                FOREIGN KEY($KEY_APR_ORD_ID) REFERENCES $TABLE_ORDENES($KEY_ORD_ID)
            )
        """)

        // 10. Tabla Ubicaciones
        db.execSQL("""
            CREATE TABLE $TABLE_UBICACIONES (
                $KEY_UBI_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_UBI_ORD_ID INTEGER,
                $KEY_UBI_LAT REAL,
                $KEY_UBI_LON REAL,
                $KEY_UBI_FECHA TEXT,
                FOREIGN KEY($KEY_UBI_ORD_ID) REFERENCES $TABLE_ORDENES($KEY_ORD_ID)
            )
        """)

        // Insertar datos iniciales
        seedData(db)
    }

    private fun seedData(db: SQLiteDatabase) {
        // Usuarios de prueba
        db.execSQL("INSERT INTO $TABLE_USUARIOS (usuario, password, nombre, rol, email) VALUES ('tecnico01', '123456', 'Técnico 01', 'Técnico', 'tecnico01@climatrack.com')")
        db.execSQL("INSERT INTO $TABLE_USUARIOS (usuario, password, nombre, rol, email) VALUES ('admin01', '123456', 'Administrador', 'Administrador', 'admin@climatrack.com')")
        db.execSQL("INSERT INTO $TABLE_USUARIOS (usuario, password, nombre, rol, email) VALUES ('cliente01', '123456', 'Cliente ACME', 'Cliente', 'cliente@acme.com')")
        
        // Clientes
        db.execSQL("INSERT INTO $TABLE_CLIENTES (nombre, telefono, direccion, email) VALUES ('ACME S.A.S', '555-0101', 'Calle 45 #12-34', 'contacto@acme.com')")
        
        // Equipos
        db.execSQL("INSERT INTO $TABLE_EQUIPOS (codigo, tipo, marca, modelo, serial, capacidad, ubicacion, cliente_id, estado) VALUES ('EQ-001', 'Split', 'Samsung', 'WindFree 24K', 'SAM-998877', '24000 BTU', 'Oficina 301', 1, 'OPERATIVO')")
        
        // Repuestos
        db.execSQL("INSERT INTO $TABLE_REPUESTOS (nombre, codigo, unidad, cantidad_disponible) VALUES ('Filtro de Aire', 'FIL-001', 'Unidad', 50)")
        db.execSQL("INSERT INTO $TABLE_REPUESTOS (nombre, codigo, unidad, cantidad_disponible) VALUES ('Gas R-410A', 'GAS-410', 'Kg', 20)")

        // Orden de prueba
        db.execSQL("""
            INSERT INTO $TABLE_ORDENES (numero, fecha, cliente_id, equipo_id, tecnico_id, tipo_servicio, descripcion, estado)
            VALUES ('OT-00025', '18/08/2026', 1, 1, 1, 'PREVENTIVO', 'Mantenimiento preventivo general', 'PENDIENTE')
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_UBICACIONES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APROBACIONES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EVIDENCIAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DETALLE_REPUESTOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REPUESTOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MANTENIMIENTOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDENES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EQUIPOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)
    }
}
