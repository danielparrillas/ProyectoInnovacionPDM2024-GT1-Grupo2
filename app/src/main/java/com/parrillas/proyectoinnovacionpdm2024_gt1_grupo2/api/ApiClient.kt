package com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient(private val tokenManager: TokenManager) {
    private val BASE_URL = "http://3.144.23.33/"

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(tokenManager))
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    inline fun <reified T> createService(): T {
        return retrofit.create(T::class.java)
    }
}

