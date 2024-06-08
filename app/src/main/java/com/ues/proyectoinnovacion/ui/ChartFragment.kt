package com.ues.proyectoinnovacion.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.ues.proyectoinnovacion.R
import com.ues.proyectoinnovacion.api.ApiClient
import com.ues.proyectoinnovacion.api.TokenManager
import com.ues.proyectoinnovacion.api.marcacion.Marcacion
import com.ues.proyectoinnovacion.api.marcacion.MarcacionResponse
import com.ues.proyectoinnovacion.api.marcacion.MarcacionService
import es.dmoral.toasty.Toasty
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class ChartFragment : Fragment() {
    private lateinit var lineChart : LineChart
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getMarcaciones()
        val view = inflater.inflate(R.layout.fragment_chart, container, false)
        lineChart = view.findViewById(R.id.line_chart)
//        alimentarGrafico(exampleMarcaciones()) // para pruebas
        return view
    }

    private fun alimentarGrafico(marcaciones: List<Marcacion>) {
        lineChart.description.text = "Marcación de asistencias"

        // separar marcaciones de entrada y salida
        val marcacionesEntrada = marcaciones.filter { it.tipo.equals("entrada", ignoreCase = true) }
        val marcacionesSalida = marcaciones.filter { it.tipo.equals("salida", ignoreCase = true) }

        // convertir marcaciones to entries
        val entriesEntrada = marcacionesEntrada.map { marcacion ->
            // convertir fecha a Date y hora a float
            val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(marcacion.fecha.split(" ")[1])
            val timeParts = marcacion.fecha.split(" ")[0].split(":").map { it.toInt() }
            val timeFloat = timeParts[0] + timeParts[1] / 60f
            Entry(date.time.toFloat(), timeFloat)
        }
        val entriesSalida = marcacionesSalida.map { marcacion ->
            val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(marcacion.fecha.split(" ")[1])
            val timeParts = marcacion.fecha.split(" ")[0].split(":").map { it.toInt() }
            val timeFloat = timeParts[0] + timeParts[1] / 60f
            Entry(date.time.toFloat(), timeFloat)
        }

        // crear LineDataSet para entrada y salida
        val dataSetEntrada = LineDataSet(entriesEntrada, "Entrada").apply {
            color = ContextCompat.getColor(requireContext(), R.color.blue)
            valueTextColor = ContextCompat.getColor(requireContext(), R.color.blue)
            valueTextSize = 15f
            setCircleColor(ContextCompat.getColor(requireContext(), R.color.blue))
            setCircleRadius(5f)
            valueFormatter = object : ValueFormatter() {
                override fun getPointLabel(entry: Entry): String {
                    val hour = entry.y.toInt()
                    val minute = ((entry.y - hour) * 60).toInt()
                    return String.format("%02d:%02d", hour, minute)
                }
            }
        }
        val dataSetSalida = LineDataSet(entriesSalida, "Salida").apply {
            color = ContextCompat.getColor(requireContext(), R.color.green)
            valueTextColor = ContextCompat.getColor(requireContext(), R.color.green)
            valueTextSize = 15f
            setCircleColor(ContextCompat.getColor(requireContext(), R.color.green))
            setCircleRadius(5f)
            valueFormatter = object : ValueFormatter() {
                override fun getPointLabel(entry: Entry): String {
                    val hour = entry.y.toInt()
                    val minute = ((entry.y - hour) * 60).toInt()
                    return String.format("%02d:%02d", hour, minute)
                }
            }
        }

        val lineData = LineData(dataSetEntrada, dataSetSalida)
        lineChart.data = lineData

        // Set the value formatter for the x-axis to display dates
        lineChart.xAxis.valueFormatter = object : ValueFormatter() {
            private val dateFormat = SimpleDateFormat("dd-MM", Locale.getDefault())
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return dateFormat.format(Date(value.toLong()))
            }
        }

        lineChart.invalidate()
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
                        alimentarGrafico(marcacionResponse.data)
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

    private fun exampleMarcaciones(): List<Marcacion> {
        return listOf(
            Marcacion(1, "entrada", "08:01 01-01-2021"),
            Marcacion(2, "salida", "17:30 01-01-2021"),
            Marcacion(3, "entrada", "07:15 02-01-2021"),
            Marcacion(4, "salida", "16:45 02-01-2021"),
            Marcacion(5, "entrada", "07:30 03-01-2021"),
            Marcacion(6, "salida", "17:15 03-01-2021"),
            Marcacion(7, "entrada", "08:45 04-01-2021"),
            Marcacion(8, "salida", "17:00 04-01-2021"),
            Marcacion(9, "entrada", "08:00 05-01-2021"),
            Marcacion(10, "salida", "17:45 05-01-2021"),
            Marcacion(11, "entrada", "08:00 06-01-2021"),
            Marcacion(12, "salida", "17:45 06-01-2021"),
            Marcacion(13, "entrada", "08:00 07-01-2021"),
            Marcacion(14, "salida", "17:45 07-01-2021"),
            Marcacion(15, "entrada", "08:00 08-01-2021"),
            Marcacion(16, "salida", "17:45 08-01-2021"),
            Marcacion(17, "entrada", "08:00 09-01-2021"),
        )
    }

}