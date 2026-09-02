# Plan de Implementación: Exclusividad por Roles (RBAC)

Este plan detalla la implementación de un sistema de Control de Acceso Basado en Roles (RBAC) para ClimaTrack, asegurando que los usuarios solo accedan a los módulos y acciones permitidos según su rol.

## Roles y Permisos Propuestos

| Rol | Acceso a Módulos | Acciones Permitidas |
| :--- | :--- | :--- |
| **Administrador** | Todos (Inicio, Órdenes, Equipos, Historial, Repuestos) | Crear, Editar, Eliminar, Ver |
| **Técnico** | Inicio, Órdenes, Historial | Ver Órdenes, Registrar Mantenimiento, Ver Historial |

> [!IMPORTANT]
> Los Técnicos tendrán acceso de "Solo Lectura" a la lista de Equipos (si se decide mantenerla visible) pero no podrán Crear, Editar ni Eliminar equipos ni repuestos.

## Propuesta de Cambios

### 1. Preparación de Datos y Sesión

#### [MODIFY] [DatabaseHelper.kt](file:///C:/Users/Aprendiz/Desktop/Clima-track-app/app/src/main/java/com/example/mantenimiento/database/DatabaseHelper.kt)
- Añadir un usuario administrador de prueba (ej. `admin01 / 123456`) con el rol `'Administrador'`.

#### [NEW] [SessionManager.kt](file:///C:/Users/Aprendiz/Desktop/Clima-track-app/app/src/main/java/com/example/mantenimiento/security/SessionManager.kt)
- Centralizar el manejo de `SharedPreferences`.
- Métodos para guardar sesión (incluyendo rol), obtener rol actual, verificar si está logueado y cerrar sesión.

### 2. Lógica de Seguridad (Middleware)

#### [NEW] [Role.kt](file:///C:/Users/Aprendiz/Desktop/Clima-track-app/app/src/main/java/com/example/mantenimiento/security/Role.kt)
- Enum para representar los roles: `ADMIN`, `TECNICO`.

#### [NEW] [SecurityMiddleware.kt](file:///C:/Users/Aprendiz/Desktop/Clima-track-app/app/src/main/java/com/example/mantenimiento/security/SecurityMiddleware.kt)
- Lógica para verificar permisos: `hasPermission(role: Role, action: Action)`.
- Método `guardActivity(activity: Activity, requiredRole: Role)` para finalizar actividades si el usuario no tiene permiso.

### 3. Integración en el Flujo de Usuario

#### [MODIFY] [LoginActivity.kt](file:///C:/Users/Aprendiz/Desktop/Clima-track-app/app/src/main/java/com/example/mantenimiento/activities/LoginActivity.kt)
- Usar `SessionManager` para guardar el rol del usuario al iniciar sesión.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/Aprendiz/Desktop/Clima-track-app/app/src/main/java/com/example/mantenimiento/MainActivity.kt)
- Ocultar elementos del `BottomNavigationView` (como "Equipos" o "Repuestos") si el rol es `TECNICO`.

#### [MODIFY] [EquiposFragment.kt](file:///C:/Users/Aprendiz/Desktop/Clima-track-app/app/src/main/java/com/example/mantenimiento/fragments/EquiposFragment.kt)
- Ocultar el `FloatingActionButton` (Añadir Equipo).
- Filtrar el `PopupMenu` para no mostrar "Editar" o "Eliminar" a los técnicos.

#### [MODIFY] [Actividades de Formulario]
- Aplicar `SecurityMiddleware.guardActivity` en `FormEquipoActivity`, `FormRepuestoActivity` y `FormOrdenActivity` para evitar accesos directos no autorizados.

## Plan de Verificación

### Pruebas Manuales
1. **Login como Técnico**:
    - Verificar que el menú de navegación no muestre opciones restringidas.
    - Intentar acceder a la creación de equipos y verificar que no es posible.
2. **Login como Administrador**:
    - Verificar acceso total a todos los botones y menús.
3. **Persistencia**:
    - Cerrar la app y volver a entrar para asegurar que el rol se mantiene correctamente.

### Seguridad Técnica
- Intentar lanzar un Intent explícito hacia `FormEquipoActivity` desde un dispositivo con sesión de Técnico y verificar que la actividad se cierra automáticamente.
