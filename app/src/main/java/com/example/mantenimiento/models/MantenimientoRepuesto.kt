package com.example.mantenimiento.models

/**
 * Relación entre un mantenimiento y los repuestos utilizados en el mismo.
 */
data class MantenimientoRepuesto(
    val id: Int? = null,
    val mantenimientoId: Int,
    val repuestoId: Int,
    val cantidadUsada: Int,
)
