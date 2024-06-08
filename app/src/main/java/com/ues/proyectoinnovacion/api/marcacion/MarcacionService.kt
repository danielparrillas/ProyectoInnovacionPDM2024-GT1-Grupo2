package com.ues.proyectoinnovacion.api.marcacion

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MarcacionService {
    @POST("perfil/marcar")
    fun marcar(@Body marcacionRequest: MarcacionRequest): Call<Void>

    @GET("perfil/mis_marcaciones")
    fun misMarcaciones(): Call<MarcacionResponse>
}