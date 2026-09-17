# Tareas: Validaciones Robustas en Creación de Órdenes

- [x] Modificar `OrdenRepository.kt`
    - [x] Implementar `existeOrdenActiva(equipoId: Int)`
    - [x] Implementar `existeOrdenEnFecha(equipoId: Int, fecha: String)`
- [x] Modificar `FormOrdenActivity.kt`
    - [x] Implementar validaciones de fecha (pasadas, futuro lejano, fines de semana)
    - [x] Implementar validación de longitud de descripción
    - [x] Integrar validaciones del repositorio en `saveOrden()`
- [x] Verificación Final
