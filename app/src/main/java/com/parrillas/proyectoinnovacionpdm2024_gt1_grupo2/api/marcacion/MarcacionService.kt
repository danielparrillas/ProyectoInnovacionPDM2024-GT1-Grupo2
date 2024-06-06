package com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.api.marcacion

import com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.api.login.LoginRequest
import com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.api.login.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MarcacionService {
    @POST("/qr/entrada")
    fun marcarEntrada(@Body marcacionRequest: MarcacionRequest): Call<MarcacionResponse>

    @POST("/qr/salida")
    fun marcarSalida(@Body marcacionRequest: MarcacionRequest): Call<MarcacionResponse>
}