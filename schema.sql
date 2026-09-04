-- Esquema de Base de Datos para ClimaTrack
-- Generado a partir de DatabaseHelper.kt (Versión 11)

-- 1. Tabla Usuarios
CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario TEXT,
    password TEXT,
    nombre TEXT,
    rol TEXT,
    email TEXT
);

-- 2. Tabla Clientes
CREATE TABLE clientes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT,
    telefono TEXT,
    direccion TEXT,
    email TEXT
);

-- 3. Tabla Equipos
CREATE TABLE equipos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    codigo TEXT,
    tipo TEXT,
    marca TEXT,
    modelo TEXT,
    serial TEXT,
    capacidad TEXT,
    ubicacion TEXT,
    cliente_id INTEGER,
    estado TEXT,
    FOREIGN KEY(cliente_id) REFERENCES clientes(id)
);

-- 4. Tabla Órdenes
CREATE TABLE ordenes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    numero TEXT,
    fecha TEXT,
    cliente_id INTEGER,
    equipo_id INTEGER,
    tecnico_id INTEGER,
    tipo_servicio TEXT,
    descripcion TEXT,
    estado TEXT,
    FOREIGN KEY(cliente_id) REFERENCES clientes(id),
    FOREIGN KEY(equipo_id) REFERENCES equipos(id),
    FOREIGN KEY(tecnico_id) REFERENCES usuarios(id)
);

-- 5. Tabla Mantenimientos
CREATE TABLE mantenimientos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    orden_id INTEGER,
    fecha TEXT,
    diagnostico TEXT,
    trabajo_realizado TEXT,
    observaciones TEXT,
    recomendaciones TEXT,
    FOREIGN KEY(orden_id) REFERENCES ordenes(id)
);

-- 6. Tabla Repuestos
CREATE TABLE repuestos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT,
    codigo TEXT,
    unidad TEXT,
    cantidad_disponible INTEGER
);

-- 7. Tabla Detalle Repuestos
CREATE TABLE detalle_repuestos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    mantenimiento_id INTEGER,
    repuesto_id INTEGER,
    cantidad INTEGER,
    FOREIGN KEY(mantenimiento_id) REFERENCES mantenimientos(id),
    FOREIGN KEY(repuesto_id) REFERENCES repuestos(id)
);

-- 8. Tabla Evidencias
CREATE TABLE evidencias (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    orden_id INTEGER,
    ruta_foto TEXT,
    fecha TEXT,
    FOREIGN KEY(orden_id) REFERENCES ordenes(id)
);

-- 9. Tabla Aprobaciones
CREATE TABLE aprobaciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    orden_id INTEGER,
    cliente TEXT,
    ruta_firma TEXT,
    aceptado INTEGER,
    fecha TEXT,
    FOREIGN KEY(orden_id) REFERENCES ordenes(id)
);

-- 10. Tabla Ubicaciones
CREATE TABLE ubicaciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    orden_id INTEGER,
    latitud REAL,
    longitud REAL,
    fecha TEXT,
    FOREIGN KEY(orden_id) REFERENCES ordenes(id)
);

-- Datos iniciales (Seed Data)
INSERT INTO usuarios (usuario, password, nombre, rol, email) VALUES ('tecnico01', '123456', 'Técnico 01', 'Técnico', 'tecnico01@climatrack.com');
INSERT INTO usuarios (usuario, password, nombre, rol, email) VALUES ('admin01', '123456', 'Administrador', 'Administrador', 'admin@climatrack.com');
INSERT INTO usuarios (usuario, password, nombre, rol, email) VALUES ('cliente01', '123456', 'Cliente ACME', 'Cliente', 'cliente@acme.com');

INSERT INTO clientes (nombre, telefono, direccion, email) VALUES ('ACME S.A.S', '555-0101', 'Calle 45 #12-34', 'contacto@acme.com');

INSERT INTO equipos (codigo, tipo, marca, modelo, serial, capacidad, ubicacion, cliente_id, estado) VALUES ('EQ-001', 'Split', 'Samsung', 'WindFree 24K', 'SAM-998877', '24000 BTU', 'Oficina 301', 1, 'OPERATIVO');

INSERT INTO repuestos (nombre, codigo, unidad, cantidad_disponible) VALUES ('Filtro de Aire', 'FIL-001', 'Unidad', 50);
INSERT INTO repuestos (nombre, codigo, unidad, cantidad_disponible) VALUES ('Gas R-410A', 'GAS-410', 'Kg', 20);

INSERT INTO ordenes (numero, fecha, cliente_id, equipo_id, tecnico_id, tipo_servicio, descripcion, estado)
VALUES ('OT-00025', '18/08/2026', 1, 1, 1, 'PREVENTIVO', 'Mantenimiento preventivo general', 'PENDIENTE');

INSERT INTO mantenimientos (orden_id, fecha, diagnostico, trabajo_realizado, observaciones, recomendaciones)
VALUES (1, '19/08/2026', 'Filtros obstruidos por polvo y falta de refrigerante.', 'Limpieza de filtros y recarga de gas R-410A.', 'El equipo presentaba vibraciones leves.', 'Realizar próxima revisión en 6 meses.');

-- 7. Tabla Detalle Repuestos (Seed)
INSERT INTO detalle_repuestos (mantenimiento_id, repuesto_id, cantidad) VALUES (1, 1, 2);
INSERT INTO detalle_repuestos (mantenimiento_id, repuesto_id, cantidad) VALUES (1, 2, 1);

-- 8. Tabla Evidencias (Seed)
INSERT INTO evidencias (orden_id, ruta_foto, fecha) VALUES (1, '/storage/emulated/0/Android/data/com.example.mantenimiento/files/Pictures/evidencia_ot25.jpg', '19/08/2026 10:30');

-- 9. Tabla Aprobaciones (Seed)
INSERT INTO aprobaciones (orden_id, cliente, ruta_firma, aceptado, fecha) VALUES (1, 'Juan Pérez', '/storage/emulated/0/Android/data/com.example.mantenimiento/files/Pictures/firma_ot25.png', 1, '19/08/2026 11:00');

-- 10. Tabla Ubicaciones (Seed)
INSERT INTO ubicaciones (orden_id, latitud, longitud, fecha) VALUES (1, 4.6097, -74.0817, '19/08/2026 10:00');
