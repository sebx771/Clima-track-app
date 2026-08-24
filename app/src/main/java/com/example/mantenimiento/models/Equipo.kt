package com.example.mantenimiento.models

/**
 * Representa un equipo de climatización en el sistema.
 */
data class Equipo(
    val id: Int? = null,
    val nombre: String,
    val marca: String,
    val modelo: String,
    val numeroSerie: String,
    val ubicacion: String,
    val cliente: String,
)
