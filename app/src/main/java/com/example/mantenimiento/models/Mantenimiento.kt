package com.example.mantenimiento.models

import java.io.Serializable

/**
 * Representa el registro de una intervención técnica realizada a un equipo vinculada a una orden.
 */
data class Mantenimiento(
    val id: Int? = null,
    val ordenId: Int,
    val fecha: String,
    val diagnostico: String,
    val trabajoRealizado: String,
    val observaciones: String,
    val recomendaciones: String,
    
    // Auxiliares para UI y trazabilidad
    var equipoNombre: String? = null,
    var numeroOrden: String? = null,
    var fotoEvidencia: String? = null,
    var firmaCliente: String? = null
) : Serializable
