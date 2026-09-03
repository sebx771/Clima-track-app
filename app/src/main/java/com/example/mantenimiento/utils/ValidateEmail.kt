package com.example.mantenimiento.utils

import android.util.Patterns

object ValidateEmail {
    /**
     * Valida si una cadena de texto tiene un formato de correo electrónico válido
     * utilizando los patrones nativos de Android.
     *
     * @param email El texto a validar.
     * @return true si es un email válido, false de lo contrario.
     */
    fun isValid(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
