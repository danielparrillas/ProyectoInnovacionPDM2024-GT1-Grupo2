package com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.api.login

import com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.api.login.LoginRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

//    @POST("auth/registrar")
//    fun registrar(@Body registrarRequest: RegistrarRequest): Call<LoginResponse>
}