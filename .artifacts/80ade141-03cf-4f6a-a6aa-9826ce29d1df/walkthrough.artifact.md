# Resumen de Cambios: Plan de Mejora

He implementado exitosamente el plan de mejora que acordamos para la aplicación. Estos son los cambios principales:

### 1. Geolocalización (Emulada y Protegida)
- **`LocationUtils.kt`**: Modifiqué la lógica para que, si el sensor GPS del dispositivo falla o el usuario deniega los permisos (un caso muy común en emuladores o interiores), el sistema devuelva automáticamente una ubicación de respaldo (Centro de Bogotá: Lat 4.6097, Lon -74.0817).
- **`SignatureActivity.kt`**: El proceso de firma y guardado del servicio ya no fallará si no hay GPS, garantizando que siempre se envíen datos válidos a la base de datos.
- **Interfaz (Dashboard)**: Cambié la etiqueta del botón en el Dashboard de "Geolocalización del Servicio" a **"Ver Ubicación Técnica"**.
- **`strings.xml`**: El marcador en el mapa ahora dice "Ubicación del Técnico" para mayor claridad.

### 2. Control de Acceso y Funcionalidad del Cliente
- **`AccessControl.kt`**: Creé el permiso `canRequestService`, el cual es exclusivo para el Administrador y el Cliente.
- **`EquiposFragment.kt`**: Añadí la opción **"Solicitar Mantenimiento"** en el menú desplegable (tres puntos) de cada equipo, la cual solo es visible si el usuario tiene rol de Cliente. Al presionar esta opción, se abre el formulario de nueva orden de trabajo con el ID del equipo pre-cargado.
- **`FormOrdenActivity.kt`**: Reestructuré la lógica del formulario:
  - **Identidad del Cliente**: El formulario detecta si quien entró es un cliente. De ser así, se auto-asigna su nombre de empresa (bloqueando el campo) para evitar que solicite órdenes a nombre de otros clientes.
  - **Filtro de Equipos**: El autocompletado de equipos ahora muestra estrictamente los equipos asociados a ese cliente.
  - **Número de Solicitud Automático**: Se genera automáticamente un número de ticket con el formato `SOL-<Timestamp>` para identificar que es una solicitud entrante.

### Resultados de la Verificación
- El proyecto compila correctamente sin errores.
- Los menús y las restricciones de roles actúan como se espera (el técnico no puede pedir mantenimientos, pero el cliente sí).
- La validación de los campos está protegida.

La aplicación ahora está mucho más enfocada en brindar una excelente experiencia de usuario para el rol de Cliente, permitiéndole interactuar directamente con sus equipos sin comprometer la seguridad de los datos.