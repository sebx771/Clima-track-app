package com.example.mantenimiento.database


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        // nombre y version de la db
        private const val DATABASE_NAME = "climatrack.db"
        private const val DATABASE_VERSION = 1

        // Nombres de Tablas
        const val TABLE_USUARIOS = "usuarios"
        const val TABLE_ORDENES = "ordenes"

        // Columnas Tabla Usuarios
        const val KEY_USR_ID = "id"
        const val KEY_USR_USUARIO = "usuario"
        const val KEY_USR_PASSWORD = "password"
        const val KEY_USR_NOMBRE = "nombre"
        const val KEY_USR_ROL = "rol"

        // Columnas Tabla Órdenes
        const val KEY_ORD_ID = "id"
        const val KEY_ORD_NUMERO = "numero"
        const val KEY_ORD_FECHA = "fecha"
        const val KEY_ORD_CLIENTE = "cliente"
        const val KEY_ORD_DIRECCION = "direccion"
        const val KEY_ORD_EQUIPO = "equipo"
        const val KEY_ORD_TIPO_SERVICIO = "tipo_servicio"
        const val KEY_ORD_DESCRIPCION = "descripcion"
        const val KEY_ORD_ESTADO = "estado"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // 1. Sentencia SQL para crear la tabla 'usuarios'
        val createUsuariosTable = """
            CREATE TABLE $TABLE_USUARIOS (
                $KEY_USR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_USR_USUARIO TEXT,
                $KEY_USR_PASSWORD TEXT,
                $KEY_USR_NOMBRE TEXT,
                $KEY_USR_ROL TEXT
            )
        """.trimIndent()

        // 2. Sentencia SQL para crear la tabla 'ordenes'
        val createOrdenesTable = """
            CREATE TABLE $TABLE_ORDENES (
                $KEY_ORD_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_ORD_NUMERO TEXT,
                $KEY_ORD_FECHA TEXT,
                $KEY_ORD_CLIENTE TEXT,
                $KEY_ORD_DIRECCION TEXT,
                $KEY_ORD_EQUIPO TEXT,
                $KEY_ORD_TIPO_SERVICIO TEXT,
                $KEY_ORD_DESCRIPCION TEXT,
                $KEY_ORD_ESTADO TEXT
            )
        """.trimIndent()

        // Ejecutar la creación de tablas
        db.execSQL(createUsuariosTable)
        db.execSQL(createOrdenesTable)

        // 3. Insertar usuario  (tecnico01 / 123456)
        db.execSQL("""
            INSERT INTO $TABLE_USUARIOS ($KEY_USR_USUARIO, $KEY_USR_PASSWORD, $KEY_USR_NOMBRE, $KEY_USR_ROL)
            VALUES ('tecnico01', '123456', 'Técnico 01', 'Técnico')
        """.trimIndent())

        // 4. Insertar órdenes de prueba iniciales para el Dashboard y la lista
        db.execSQL("""
            INSERT INTO $TABLE_ORDENES ($KEY_ORD_NUMERO, $KEY_ORD_FECHA, $KEY_ORD_CLIENTE, $KEY_ORD_DIRECCION, $KEY_ORD_EQUIPO, $KEY_ORD_TIPO_SERVICIO, $KEY_ORD_DESCRIPCION, $KEY_ORD_ESTADO)
            VALUES 
            ('OT-00025', '18/08/2026', 'ACME S.A.S', 'Calle 45 #12-34', 'Split Inverter 24K', 'PREVENTIVO', 'Mantenimiento preventivo general', 'PENDIENTE'),
            ('OT-00026', '18/08/2026', 'Clínica del Norte', 'Carrera 50 #80-10', 'Cassette 36K', 'CORRECTIVO', 'Fuga de refrigerante', 'EN PROCESO'),
            ('OT-00027', '19/08/2026', 'Hotel Caribe', 'Av. del Mar #5-12', 'Chiller 50TR', 'INSPECCIÓN', 'Revisión de parámetros', 'FINALIZADA')
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDENES")
        onCreate(db)
    }
}