package com.example.mantenimiento.models

import java.io.Serializable

/**
 * Representa un equipo de climatización en el sistema.
 */
data class Equipo(
    val id: Int? = null,
    val codigo: String,
    val tipo: String,
    val marca: String,
    val modelo: String,
    val serial: String,
    val capacidad: String,
    val ubicacion: String,
    val clienteId: Int,
    val estado: String,
    
    // Auxiliar
    var clienteNombre: String? = null
) : Serializable
