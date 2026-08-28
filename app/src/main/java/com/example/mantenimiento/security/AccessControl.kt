package com.example.mantenimiento.security

import android.content.Context

object AccessControl {

    fun canManageInventory(role: Role): Boolean = role == Role.ADMIN

    fun canCreateEquipment(role: Role): Boolean = role == Role.ADMIN

    fun canViewOrders(role: Role): Boolean = role == Role.ADMIN || role == Role.TECNICO

    fun canRegisterMaintenance(role: Role): Boolean = role == Role.ADMIN || role == Role.TECNICO

    fun canEditOrDelete(role: Role): Boolean = role == Role.ADMIN
}
