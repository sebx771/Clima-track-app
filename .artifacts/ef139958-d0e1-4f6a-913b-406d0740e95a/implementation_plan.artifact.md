# Plan de Implementación: Validaciones Robustas en Creación de Órdenes

Este plan detalla la implementación de reglas de negocio para evitar datos erróneos o duplicados al crear órdenes de mantenimiento.

## Cambios Propuestos

### [Repositorio de Datos]
#### [MODIFY] [OrdenRepository.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/Clima-track-app/app/src/main/java/com/example/mantenimiento/repository/OrdenRepository.kt)
- Añadir `existeOrdenActiva(equipoId: Int): Boolean`: Verifica si el equipo tiene órdenes en estado 'PENDIENTE' o 'EN PROCESO'.
- Añadir `existeOrdenEnFecha(equipoId: Int, fecha: String): Boolean`: Verifica si ya se registró una solicitud para ese equipo en la fecha indicada.

### [Interfaz de Usuario]
#### [MODIFY] [FormOrdenActivity.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/Clima-track-app/app/src/main/java/com/example/mantenimiento/activities/FormOrdenActivity.kt)
- **Validación de Fecha:**
    - Impedir selección de fechas pasadas.
    - Limitar a un máximo de 60 días a futuro.
    - Bloquear registros en fines de semana (Sábados y Domingos).
- **Validación de Texto:**
    - Requerir una descripción mínima de 20 caracteres para asegurar claridad técnica.
- **Validación Cruzada (DB):**
    - Llamar a los nuevos métodos del repositorio antes de guardar.

## Reglas de Negocio a Implementar
| Regla | Acción | Mensaje al Usuario |
| :--- | :--- | :--- |
| Duplicidad Diaria | Si existe orden para el mismo equipo hoy | "Ya existe una solicitud para este equipo hoy" |
| Órdenes Activas | Si el equipo está en mantenimiento o pendiente | "El equipo ya tiene una orden activa pendiente" |
| Fecha Pasada | Si la fecha es < hoy | "No se pueden crear órdenes en fechas pasadas" |
| Fecha Futura | Si la fecha es > 60 días | "La fecha no puede superar los 60 días a futuro" |
| Fines de Semana | Si es Sábado o Domingo | "No se agendan mantenimientos los fines de semana" |
| Descripción Corta | Si tiene < 20 caracteres | "La descripción debe tener al menos 20 caracteres" |

## Verificación
1. Intentar crear una orden con fecha de ayer (Debe fallar).
2. Intentar crear una orden un domingo (Debe fallar).
3. Intentar crear una orden para un equipo que ya tiene una orden "PENDIENTE" (Debe fallar).
4. Escribir "revisar" en la descripción (Debe fallar por longitud).
5. Crear una orden válida y luego intentar crear otra exactamente igual para el mismo equipo y día (Debe fallar).
