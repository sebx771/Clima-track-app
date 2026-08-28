package com.example.mantenimiento.security

enum class Role(val value: String) {
    ADMIN("Administrador"),
    TECNICO("Técnico"),
    CLIENTE("Cliente");

    companion object {
        fun fromString(value: String?): Role {
            return values().find { it.value.equals(value, ignoreCase = true) } ?: TECNICO
        }
    }
}
