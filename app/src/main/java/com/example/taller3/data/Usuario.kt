package com.example.taller3.data

import kotlinx.serialization.Serializable

@Serializable
class Usuario (
    val email: String,
    val contraseña : String,
    val nombreUsuario: String,
    val numeroTelefono: Int,
    val Activo: Boolean

)