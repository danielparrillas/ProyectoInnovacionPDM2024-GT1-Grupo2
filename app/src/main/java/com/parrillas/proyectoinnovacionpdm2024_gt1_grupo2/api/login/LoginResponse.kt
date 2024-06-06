package com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.api.login

data class LoginResponse(
    val access_token: String,
    val is_admin: Boolean
)
