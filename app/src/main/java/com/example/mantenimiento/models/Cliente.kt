package com.example.mantenimiento.models

import java.io.Serializable

data class Cliente(
    val id: Int? = null,
    val nombre: String,
    val telefono: String,
    val direccion: String,
    val email: String
) : Serializable
