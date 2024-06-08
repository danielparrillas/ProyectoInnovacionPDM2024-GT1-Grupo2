package com.ues.proyectoinnovacion.api.marcacion

data class  Marcacion(
    val id: Int,
    val tipo: String,
    val fecha: String,
)
data class MarcacionResponse(
    val data : List<Marcacion>
)
