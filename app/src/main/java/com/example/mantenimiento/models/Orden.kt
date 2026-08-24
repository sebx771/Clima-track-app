package com.example.mantenimiento.models

data class Orden(
    val id: Int,
    val numero: String,
    val fecha: String,
    val cliente: String,
    val direccion: String,
    val equipo: String,
    val tipoServicio: String,
    val descripcion: String,
    val estado: String
)