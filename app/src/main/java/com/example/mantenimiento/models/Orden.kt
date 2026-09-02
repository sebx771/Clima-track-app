package com.example.mantenimiento.models

import java.io.Serializable

data class Orden(
    val id: Int? = null,
    val numero: String,
    val fecha: String,
    val clienteId: Int,
    val equipoId: Int,
    val tecnicoId: Int?,
    val tipoServicio: String,
    val descripcion: String,
    val estado: String,
    
    // Auxiliares para UI
    var clienteNombre: String? = null,
    var equipoNombre: String? = null,
    var tecnicoNombre: String? = null
) : Serializable
