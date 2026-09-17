# Walkthrough: Validaciones Robustas en Creación de Órdenes

He implementado un conjunto de validaciones tanto en el cliente (UI) como en la persistencia (DB) para asegurar que las órdenes de mantenimiento se creen bajo reglas de negocio estrictas.

## Cambios Realizados

### 1. Mejoras en el Repositorio de Órdenes
Se añadieron métodos para consultar el estado del equipo antes de permitir una nueva solicitud.
- `existeOrdenActiva(equipoId: Int)`: Detecta si hay trabajos en curso o pendientes.
- `existeOrdenEnFecha(equipoId: Int, fecha: String)`: Evita duplicados para el mismo día.
- **Archivo:** [OrdenRepository.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/Clima-track-app/app/src/main/java/com/example/mantenimiento/repository/OrdenRepository.kt)

### 2. Restricciones de Fecha Inteligentes
El selector de fecha (`DatePickerDialog`) ahora es más restrictivo para evitar errores comunes:
- **Mínimo:** No se permiten fechas pasadas (hoy es el límite inferior).
- **Máximo:** Se limitó la agenda a un máximo de 60 días a futuro.
- **Días Laborales:** Si el usuario selecciona un Sábado o Domingo, el sistema bloquea la selección con un mensaje informativo.
- **Archivo:** [FormOrdenActivity.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/Clima-track-app/app/src/main/java/com/example/mantenimiento/activities/FormOrdenActivity.kt)

### 3. Calidad de la Información
- Se añadió una validación de longitud mínima (20 caracteres) para la descripción del servicio. Esto obliga al usuario a proporcionar contexto útil para el técnico.
- Se integraron todas las validaciones del repositorio en el flujo de guardado.

## Resumen de Reglas Aplicadas
| Validación | Tipo | Acción |
| :--- | :--- | :--- |
| **Fecha Pasada** | UI | Bloqueado en el calendario |
| **Fines de Semana** | UI | Mensaje de advertencia y borrado de selección |
| **Duplicidad Diaria** | DB | Error: "Ya existe una solicitud hoy" |
| **Órdenes Activas** | DB | Error: "El equipo ya tiene una orden activa" |
| **Descripción** | Lógica | Mínimo 20 caracteres |

render_diffs(file:///C:/Users/Aprendiz/AndroidStudioProjects/Clima-track-app/app/src/main/java/com/example/mantenimiento/repository/OrdenRepository.kt)
render_diffs(file:///C:/Users/Aprendiz/AndroidStudioProjects/Clima-track-app/app/src/main/java/com/example/mantenimiento/activities/FormOrdenActivity.kt)
