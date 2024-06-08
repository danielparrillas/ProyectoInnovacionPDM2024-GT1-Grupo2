package com.ues.proyectoinnovacion.ui.marcaciones

import MarcacionAdapter
import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import es.dmoral.toasty.Toasty
import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableSheet
import java.io.File

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
                    Toasty.error(
                        requireContext(),
                        "Error al obtener las marcaciones",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<MarcacionResponse>, t: Throwable) {
                Log.e("FragmentMarcaciones", "Error al obtener las marcaciones", t)
                Toasty.error(
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

    private fun exportarMarcaciones() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/vnd.ms-excel"
            putExtra(Intent.EXTRA_TITLE, "marcaciones.xls")
        }
        startActivityForResult(intent, CREATE_FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                writeExcelFile(uri)
            }
        }
    }

    private fun writeExcelFile(uri: Uri) {
        val adapter = lvMarcaciones.adapter as MarcacionAdapter
        val marcaciones = adapter.getMarcaciones()

        val outputStream = requireContext().contentResolver.openOutputStream(uri)
        val workbook = Workbook.createWorkbook(outputStream)
        val sheet: WritableSheet = workbook.createSheet("Marcaciones", 0)

        // Add headers
        sheet.addCell(Label(0, 0, "ID"))
        sheet.addCell(Label(1, 0, "Tipo"))
        sheet.addCell(Label(2, 0, "Fecha"))

        // Add data
        marcaciones?.forEachIndexed { index, marcacion ->
            sheet.addCell(Label(0, index + 1, marcacion.id.toString()))
            sheet.addCell(Label(1, index + 1, marcacion.tipo))
            sheet.addCell(Label(2, index + 1, marcacion.fecha))
        }

        workbook.write()
        workbook.close()

        Toasty.success(requireContext(), "Marcaciones exportadas", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val CREATE_FILE_REQUEST_CODE = 1
    }

}