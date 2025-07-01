// KlasifikasiFragment.kt
package com.example.klafka.ui.clasification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.klafka.R
import com.example.klafka.databinding.FragmentKlasifikasiBinding

class KlasifikasiFragment : Fragment() {

    private var _binding: FragmentKlasifikasiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKlasifikasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnKlasifikasiLangsung.setOnClickListener {
            findNavController().navigate(R.id.action_klasifikasiFragment_to_klasifikasiLangsungFragment)
        }

        binding.btnKlasifikasiKonveksi.setOnClickListener {
            findNavController().navigate(R.id.action_klasifikasiFragment_to_klasifikasiKonveksiFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
