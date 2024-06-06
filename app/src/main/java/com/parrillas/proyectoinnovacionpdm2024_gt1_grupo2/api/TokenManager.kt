package com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.api

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("grupo-2-innovacion", Context.MODE_PRIVATE)

    companion object {
        private const val TOKEN_KEY = "auth_token"
    }

    fun saveToken(token: String) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(TOKEN_KEY, null)
    }

    fun clearToken() {
        prefs.edit().remove(TOKEN_KEY).apply()
    }
}
