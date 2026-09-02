package com.example.mantenimiento.models

/**
 * Representa un repuesto o insumo disponible en el catálogo.
 */
data class Repuesto(
    val id: Int? = null,
    val nombre: String,
    val codigo: String,
    val unidad: String? = null,
    val cantidadDisponible: Int,
)
