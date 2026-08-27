package com.example.mantenimiento.database


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        // nombre y version de la db
        private const val DATABASE_NAME = "climatrack.db"
        private const val DATABASE_VERSION = 4

        // Nombres de Tablas
        const val TABLE_USUARIOS = "usuarios"
        const val TABLE_ORDENES = "ordenes"
        const val TABLE_EQUIPOS = "equipos"
        const val TABLE_MANTENIMIENTOS = "mantenimientos"
        const val TABLE_REPUESTOS = "repuestos"
        const val TABLE_MANTENIMIENTO_REPUESTOS = "mantenimiento_repuestos"

        // Columnas Tabla Usuarios
        const val KEY_USR_ID = "id"
        const val KEY_USR_USUARIO = "usuario"
        const val KEY_USR_PASSWORD = "password"
        const val KEY_USR_NOMBRE = "nombre"
        const val KEY_USR_ROL = "rol"
        const val KEY_USR_EMAIL = "email"

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
        const val KEY_ORD_LATITUD = "latitud"
        const val KEY_ORD_LONGITUD = "longitud"
        const val KEY_ORD_FOTO_RUTA = "foto_ruta"
        const val KEY_ORD_FIRMA_RUTA = "firma_ruta"

        // Columnas Tabla Equipos
        const val KEY_EQP_ID = "id"
        const val KEY_EQP_NOMBRE = "nombre"
        const val KEY_EQP_MARCA = "marca"
        const val KEY_EQP_MODELO = "modelo"
        const val KEY_EQP_SERIE = "numero_serie"
        const val KEY_EQP_UBICACION = "ubicacion"
        const val KEY_EQP_CLIENTE = "cliente"

        // Columnas Tabla Mantenimientos
        const val KEY_MNT_ID = "id"
        const val KEY_MNT_EQP_ID = "equipo_id"
        const val KEY_MNT_FECHA = "fecha"
        const val KEY_MNT_TIPO = "tipo"
        const val KEY_MNT_DESC = "descripcion"
        const val KEY_MNT_OBS = "observaciones"
        const val KEY_MNT_ESTADO = "estado_final"
        const val KEY_MNT_FOTO = "foto_evidencia"
        const val KEY_MNT_FIRMA = "firma_cliente"

        // Columnas Tabla Repuestos
        const val KEY_REP_ID = "id"
        const val KEY_REP_NOMBRE = "nombre"
        const val KEY_REP_CODIGO = "codigo"
        const val KEY_REP_STOCK = "cantidad_disponible"

        // Columnas Tabla MantenimientoRepuestos
        const val KEY_MR_ID = "id"
        const val KEY_MR_MNT_ID = "mantenimiento_id"
        const val KEY_MR_REP_ID = "repuesto_id"
        const val KEY_MR_CANT = "cantidad_usada"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // 1. Sentencia SQL para crear la tabla 'usuarios'
        val createUsuariosTable = """
            CREATE TABLE $TABLE_USUARIOS (
                $KEY_USR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_USR_USUARIO TEXT,
                $KEY_USR_EMAIL TEXT,
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
                $KEY_ORD_ESTADO TEXT,
                $KEY_ORD_LATITUD TEXT,
                $KEY_ORD_LONGITUD TEXT,
                $KEY_ORD_FOTO_RUTA TEXT,
                $KEY_ORD_FIRMA_RUTA TEXT
            )
        """.trimIndent()

        // 3. Sentencia SQL para crear la tabla 'equipos'
        val createEquiposTable = """
            CREATE TABLE $TABLE_EQUIPOS (
                $KEY_EQP_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_EQP_NOMBRE TEXT,
                $KEY_EQP_MARCA TEXT,
                $KEY_EQP_MODELO TEXT,
                $KEY_EQP_SERIE TEXT,
                $KEY_EQP_UBICACION TEXT,
                $KEY_EQP_CLIENTE TEXT
            )
        """.trimIndent()

        // 4. Sentencia SQL para crear la tabla 'mantenimientos'
        val createMantenimientosTable = """
            CREATE TABLE $TABLE_MANTENIMIENTOS (
                $KEY_MNT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_MNT_EQP_ID INTEGER,
                $KEY_MNT_FECHA TEXT,
                $KEY_MNT_TIPO TEXT,
                $KEY_MNT_DESC TEXT,
                $KEY_MNT_OBS TEXT,
                $KEY_MNT_ESTADO TEXT,
                $KEY_MNT_FOTO TEXT,
                $KEY_MNT_FIRMA TEXT,
                FOREIGN KEY($KEY_MNT_EQP_ID) REFERENCES $TABLE_EQUIPOS($KEY_EQP_ID)
            )
        """.trimIndent()

        // 5. Sentencia SQL para crear la tabla 'repuestos'
        val createRepuestosTable = """
            CREATE TABLE $TABLE_REPUESTOS (
                $KEY_REP_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_REP_NOMBRE TEXT,
                $KEY_REP_CODIGO TEXT,
                $KEY_REP_STOCK INTEGER
            )
        """.trimIndent()

        // 6. Sentencia SQL para crear la tabla intermedia 'mantenimiento_repuestos'
        val createMNTREPTable = """
            CREATE TABLE $TABLE_MANTENIMIENTO_REPUESTOS (
                $KEY_MR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_MR_MNT_ID INTEGER,
                $KEY_MR_REP_ID INTEGER,
                $KEY_MR_CANT INTEGER,
                FOREIGN KEY($KEY_MR_MNT_ID) REFERENCES $TABLE_MANTENIMIENTOS($KEY_MNT_ID),
                FOREIGN KEY($KEY_MR_REP_ID) REFERENCES $TABLE_REPUESTOS($KEY_REP_ID)
            )
        """.trimIndent()

        // Ejecutar la creación de tablas
        db.execSQL(createUsuariosTable)
        db.execSQL(createOrdenesTable)
        db.execSQL(createEquiposTable)
        db.execSQL(createMantenimientosTable)
        db.execSQL(createRepuestosTable)
        db.execSQL(createMNTREPTable)

        // 7. Insertar usuario  (tecnico01 / 123456)
        db.execSQL("""
            INSERT INTO $TABLE_USUARIOS ($KEY_USR_USUARIO, $KEY_USR_PASSWORD, $KEY_USR_NOMBRE,$KEY_USR_EMAIL, $KEY_USR_ROL)
            VALUES ('tecnico01', '123456', 'Técnico 01','tecnico@gmail.com' ,'Técnico')
        """.trimIndent())

        // 8. Insertar órdenes de prueba
        db.execSQL("""
            INSERT INTO $TABLE_ORDENES ($KEY_ORD_NUMERO, $KEY_ORD_FECHA, $KEY_ORD_CLIENTE, $KEY_ORD_DIRECCION, $KEY_ORD_EQUIPO, $KEY_ORD_TIPO_SERVICIO, $KEY_ORD_DESCRIPCION, $KEY_ORD_ESTADO)
            VALUES 
            ('OT-00025', '18/08/2026', 'ACME S.A.S', 'Calle 45 #12-34', 'Split Inverter 24K', 'PREVENTIVO', 'Mantenimiento preventivo general', 'PENDIENTE'),
            ('OT-00026', '18/08/2026', 'Clínica del Norte', 'Carrera 50 #80-10', 'Cassette 36K', 'CORRECTIVO', 'Fuga de refrigerante', 'EN PROCESO'),
            ('OT-00027', '19/08/2026', 'Hotel Caribe', 'Av. del Mar #5-12', 'Chiller 50TR', 'INSPECCIÓN', 'Revisión de parámetros', 'FINALIZADA')
        """.trimIndent())

        // 9. Insertar equipos de prueba
        db.execSQL("""
            INSERT INTO $TABLE_EQUIPOS ($KEY_EQP_NOMBRE, $KEY_EQP_MARCA, $KEY_EQP_MODELO, $KEY_EQP_SERIE, $KEY_EQP_UBICACION, $KEY_EQP_CLIENTE)
            VALUES 
            ('Aire Oficina Gerencia', 'Samsung', 'WindFree 24K', 'SAM-998877', 'Oficina 301', 'ACME S.A.S'),
            ('Chiller Central', 'York', 'YLAA-050', 'YORK-112233', 'Azotea Bloque A', 'Clínica del Norte')
        """.trimIndent())

        // 10. Insertar repuestos de prueba
        db.execSQL("""
            INSERT INTO $TABLE_REPUESTOS ($KEY_REP_NOMBRE, $KEY_REP_CODIGO, $KEY_REP_STOCK)
            VALUES 
            ('Filtro de Aire 24K', 'FIL-001', 50),
            ('Gas R-410A (Kg)', 'GAS-410', 20),
            ('Capacitor 45uF', 'CAP-045', 15)
        """.trimIndent())

        // 11. Insertar mantenimientos de prueba con evidencias
        db.execSQL("""
            INSERT INTO $TABLE_MANTENIMIENTOS ($KEY_MNT_EQP_ID, $KEY_MNT_FECHA, $KEY_MNT_TIPO, $KEY_MNT_DESC, $KEY_MNT_OBS, $KEY_MNT_ESTADO, $KEY_MNT_FOTO, $KEY_MNT_FIRMA)
            VALUES 
            (1, '20/08/2026', 'PREVENTIVO', 'Limpieza de filtros y serpentín', 'Todo en orden', 'OPERATIVO', 'sample_foto_1.jpg', 'sample_firma_1.png'),
            (2, '21/08/2026', 'CORRECTIVO', 'Cambio de capacitor de arranque', 'Se recomienda revisión en 15 días', 'OPERATIVO', 'sample_foto_2.jpg', 'sample_firma_2.png')
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDENES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EQUIPOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MANTENIMIENTOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REPUESTOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MANTENIMIENTO_REPUESTOS")
        onCreate(db)
    }
}
