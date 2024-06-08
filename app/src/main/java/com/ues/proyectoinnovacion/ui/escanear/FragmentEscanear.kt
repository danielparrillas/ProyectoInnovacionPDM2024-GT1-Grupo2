package com.ues.proyectoinnovacion.ui.escanear

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.ues.proyectoinnovacion.R
import com.ues.proyectoinnovacion.api.ApiClient
import com.ues.proyectoinnovacion.api.TokenManager
import com.ues.proyectoinnovacion.api.marcacion.MarcacionRequest
import com.ues.proyectoinnovacion.api.marcacion.MarcacionService
import es.dmoral.toasty.Toasty


private const val CAMERA_REQUEST_CODE = 101

class FragmentEscanear: Fragment() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPermissions()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_escanear, container, false)
        initComponents(view)
        initListeners()
        return view
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toasty.info(
                        requireContext(),
                        "You need the camera permission to be able to use this app!",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.i("ScannerFragment", "Permission has been denied by user")
                } else {
                    Log.i("ScannerFragment", "Permission has been granted by user")
                }
            }
        }
    }

    fun initComponents(view: View){
        val scannerView = view.findViewById<CodeScannerView>(R.id.escaner)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                this@FragmentEscanear.marcar(it.text)
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun initListeners(){
        codeScanner.startPreview()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("ScannerFragment", "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    private fun marcar(codigo: String){
        val tokenManager = TokenManager(requireContext())
        val apiClient = ApiClient(tokenManager)
        val request = MarcacionRequest(codigo)
        val call = apiClient.createService<MarcacionService>().marcar(request)

        call.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(
                call: retrofit2.Call<Void>,
                response: retrofit2.Response<Void>
            ) {
                if (response.isSuccessful){
                    Toasty.success(requireContext(), "Se registro la marca", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toasty.error(
                        requireContext(),
                        "Hubo un error al marcar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(
                call: retrofit2.Call<Void>,
                t: Throwable
            ) {
                Toasty.error(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}