package com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.api.ApiClient
import com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.api.TokenManager
import com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.api.login.LoginRequest
import com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.api.login.LoginResponse
import com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.api.login.LoginService

class LoginActivity : AppCompatActivity() {
    private lateinit var bLogin: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
        initListener()
    }

    private fun initComponents() {
        bLogin = findViewById(R.id.bLogin)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
    }

    private fun initListener() {
        bLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email y contraseña son requeridos", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(
                            this@LoginActivity,
                            "Bienvenido",
                            Toast.LENGTH_SHORT
                        ).show()
                        tokenManager.saveToken(loginResponse.access_token)
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Credenciales incorrectas",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                call: retrofit2.Call<LoginResponse>,
                t: Throwable
            ) {
                Toast.makeText(this@LoginActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

}