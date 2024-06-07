package com.ues.proyectoinnovacion.api.marcacion

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MarcacionService {
    @POST("perfil/marcar")
    fun marcarEntrada(@Body marcacionRequest: MarcacionRequest): Call<Void>
}