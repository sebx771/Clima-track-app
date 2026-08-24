package com.example.mantenimiento.models

/**
 * Representa el registro de una intervención técnica realizada a un equipo.
 */
data class Mantenimiento(
    val id: Int? = null,
    val equipoId: Int,
    val fecha: String,
    val tipo: String, // Preventivo, Correctivo, Inspección
    val descripcion: String,
    val observaciones: String,
    val estadoFinal: String, // Operativo, Fuera de Servicio
    var equipoNombre: String? = null,
)
