package com.example.klafka.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.klafka.R
import com.example.klafka.adapter.HistoryAdapter
import com.example.klafka.databinding.FragmentHistoryBinding
import com.example.klafka.helper.HistoryDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HistoryAdapter
    private lateinit var dbHelper: HistoryDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = HistoryDatabaseHelper(requireContext())

        adapter = HistoryAdapter(
            requireContext(),
            emptyList(),
            onItemClick = { history ->
                val bundle = Bundle().apply {
                    putString("jenisKlasifikasi", history.jenisKlasifikasi)
                    putString("namaJenisKain", history.namaJenisKain)
                    putString("tanggal", history.tanggal)
                    putString("imagePath", history.imagePath)
                }
                findNavController().navigate(R.id.historyDetailFragment, bundle)
            },
            onDeleteClick = { history ->
                if (dbHelper.deleteHistory(history.id)) {
                    loadHistory()
                    Toast.makeText(requireContext(), "Riwayat dihapus", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Gagal menghapus riwayat", Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHistory.adapter = adapter

        loadHistory()
    }

    private fun loadHistory() {
        // Tampilkan progress loading
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewHistory.visibility = View.GONE

        // Panggil database di background
        lifecycleScope.launch(Dispatchers.IO) {
            val data = dbHelper.getAllHistory()
            withContext(Dispatchers.Main) {
                adapter.updateData(data)
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewHistory.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
