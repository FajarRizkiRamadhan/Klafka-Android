package com.example.klafka.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.klafka.databinding.FragmentDetailItemJenisKainBinding
import com.example.klafka.model.JenisKain

class KainDetailFragment : Fragment() {

    private var _binding: FragmentDetailItemJenisKainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailItemJenisKainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Ambil data dari argument bundle
        val kain = arguments?.getParcelable<JenisKain>("item")
        if (kain == null){
            Log.e("DetialFragment","Jenis kain is null")
        }

        kain?.let {
            binding.detailFabricName.text = it.nama
            binding.detailFabricDesc.text = it.deskripsi
            binding.detailFabricUsage.text = it.kegunaan
            binding.detailFabricCharacteristics.text = it.karakteristik
            binding.detailFabricCare.text = it.perawatan
            binding.detailFabricImage.setImageResource(it.gambarResId)
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
