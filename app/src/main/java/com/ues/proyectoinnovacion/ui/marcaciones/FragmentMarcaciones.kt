package com.ues.proyectoinnovacion.ui.marcaciones

import MarcacionAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ues.proyectoinnovacion.R
import com.ues.proyectoinnovacion.api.ApiClient
import com.ues.proyectoinnovacion.api.TokenManager
import com.ues.proyectoinnovacion.api.marcacion.Marcacion
import com.ues.proyectoinnovacion.api.marcacion.MarcacionResponse
import com.ues.proyectoinnovacion.api.marcacion.MarcacionService

class FragmentMarcaciones : Fragment() {
    private lateinit var bExportar: Button
    private lateinit var lvMarcaciones: ListView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        getMarcaciones()
        val view = inflater.inflate(R.layout.fragment_mis_marcaciones, container, false)
        initComponents(view)
        initListeners()
        return view
    }

    private fun initComponents(view: View) {
        bExportar = view.findViewById(R.id.bExportar)
        lvMarcaciones = view.findViewById(R.id.lvMarcaciones)
    }

    private fun initListeners() {
        bExportar.setOnClickListener { exportarMarcaciones() }
    }

    private fun getMarcaciones() {
        val tokenManager = TokenManager(requireContext())
        val apiClient = ApiClient(tokenManager)
        val call = apiClient.createService<MarcacionService>().misMarcaciones()
        call.enqueue(object : retrofit2.Callback<MarcacionResponse> {
            override fun onResponse(
                call: retrofit2.Call<MarcacionResponse>,
                response: retrofit2.Response<MarcacionResponse>
            ) {
                if (response.isSuccessful) {
                    val marcacionResponse = response.body()
                    if (marcacionResponse != null) {
                        llenarLista(marcacionResponse.data)
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error al obtener las marcaciones",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<MarcacionResponse>, t: Throwable) {
                Log.e("FragmentMarcaciones", "Error al obtener las marcaciones", t)
                Toast.makeText(
                    requireContext(),
                    "Error al obtener las marcaciones",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        )
    }

    private fun llenarLista(marcaciones: List<Marcacion>) {
        val adapter = MarcacionAdapter(requireContext(), marcaciones)
        lvMarcaciones.adapter = adapter
    }

    private fun exportarMarcaciones() {}

}