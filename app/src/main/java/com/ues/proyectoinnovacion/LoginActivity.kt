package com.ues.proyectoinnovacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.ues.proyectoinnovacion.api.ApiClient
import com.ues.proyectoinnovacion.api.TokenManager
import com.ues.proyectoinnovacion.api.login.LoginRequest
import com.ues.proyectoinnovacion.api.login.LoginResponse
import com.ues.proyectoinnovacion.api.login.LoginService
import es.dmoral.toasty.Toasty

class LoginActivity : AppCompatActivity() {
    private lateinit var bLogin: Button
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        initComponents()
        initListener()
    }

    private fun initComponents() {
        bLogin = findViewById(R.id.btnIniciarSesion)
        etEmail = findViewById(R.id.etCorreo)
        etPassword = findViewById(R.id.etContrasena)
    }

    private fun initListener() {
        bLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toasty.warning(this, "Email y contraseña son requeridos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        val tokenManager = TokenManager(this)
        val apiClient = ApiClient(tokenManager)
        val loginRequest = LoginRequest(email, password)
        val call = apiClient.createService<LoginService>().login(loginRequest)

        call.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(
                call: retrofit2.Call<LoginResponse>,
                response: retrofit2.Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Toasty.success(
                            this@LoginActivity,
                            "Bienvenido",
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                        tokenManager.saveToken(loginResponse.access_token)
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    Toasty.error(
                        this@LoginActivity,
                        "Credenciales incorrectas",
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                }
            }

            override fun onFailure(
                call: retrofit2.Call<LoginResponse>,
                t: Throwable
            ) {
                Toasty.error(this@LoginActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}