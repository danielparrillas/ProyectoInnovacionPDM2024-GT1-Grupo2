package com.ues.proyectoinnovacion.api.login

data class LoginResponse(
    val access_token: String,
    val is_admin: Boolean
)
