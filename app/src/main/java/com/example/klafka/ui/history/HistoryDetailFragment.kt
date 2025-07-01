package com.example.klafka.ui.history

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.klafka.databinding.FragmentHistoryDetailBinding
import com.example.klafka.repository.JenisKainRepository
import com.example.klafka.ui.home.MainActivity

class HistoryDetailFragment : Fragment() {

    private var _binding: FragmentHistoryDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.showBottomNav(false)

        val args = arguments
        val jenisKlasifikasi = args?.getString("jenisKlasifikasi")
        val namaJenisKain = args?.getString("namaJenisKain")
        val tanggal = args?.getString("tanggal")
        val imagePath = args?.getString("imagePath")

        imagePath?.let {
            binding.detailFabricImage.setImageURI(Uri.parse(it))
        }

        // Cari deskripsi dari repository berdasarkan nama kain
        val kain = JenisKainRepository.getData().find {
            it.nama.equals(namaJenisKain, ignoreCase = true)
        }



        if (kain != null) {
            binding.detailFabricName.text = kain.nama
            binding.tvDetailJenisKlasifikasi.text = jenisKlasifikasi
            binding.tvDetailTanggal.text = tanggal
            binding.detailFabricDesc.text = kain.deskripsi
            binding.detailFabricUsage.text = kain.kegunaan
            binding.detailFabricCharacteristics.text = kain.karakteristik
            binding.detailFabricCare.text = kain.perawatan
        } else {
            binding.detailFabricName.text = "Jenis kain tidak ditemukan"
            binding.detailFabricDesc.text = "-"
            binding.detailFabricUsage.text = "-"
            binding.detailFabricCharacteristics.text = "-"
            binding.detailFabricCare.text = "-"
        }


        // Tombol kembali
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
