package com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.databinding.FragmentHomeBinding
import com.parrillas.proyectoinnovacionpdm2024_gt1_grupo2.R

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var fabScanner: FloatingActionButton
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =inflater.inflate(R.layout.fragment_home, container, false)
        initComponents(view)
        initUI()
        initListeners()
        return view
    }

    fun initComponents(view: View) {
        fabScanner = view.findViewById(R.id.fabScanner)
    }

    fun initUI() {}
    fun initListeners() {
        fabScanner.setOnClickListener {
            findNavController().navigate(R.id.nav_qr_scanner)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}