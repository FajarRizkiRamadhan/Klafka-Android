package com.example.klafka.ui.konveksi

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.klafka.R
import com.example.klafka.adapter.KonveksiAdapter
import com.example.klafka.databinding.FragmentKonveksiBinding
import com.example.klafka.helper.KonveksiDatabaseHelper
import com.example.klafka.model.KonveksiBatch
import com.example.klafka.model.KonveksiProfile
import com.example.klafka.util.ExcelExporter
import com.example.klafka.util.PdfExporter

class KonveksiFragment : Fragment() {

    private var _binding: FragmentKonveksiBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: KonveksiAdapter

    private lateinit var dbHelper: KonveksiDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKonveksiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = KonveksiDatabaseHelper(requireContext())

        adapter = KonveksiAdapter(emptyList()) { batch ->
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Hapus Riwayat")
                .setMessage("Apakah Anda yakin ingin menghapus data batch ini?")
                .setPositiveButton("Hapus") { _, _ ->
                    if (dbHelper.deleteBatch(batch.id)) {
                        Toast.makeText(requireContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                        loadBatchData()
                    } else {
                        Toast.makeText(requireContext(), "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Batal", null)
                .create()

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_blue))
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_blue))
            }

            dialog.show()
        }

        binding.rvRiwayatKonveksi.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRiwayatKonveksi.adapter = adapter


        binding.btnEditProfil.setOnClickListener {
            dbHelper.getProfile()?.let { profile ->
                showEditDialog(profile)
            } ?: Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
        }

        updateUI()
        loadBatchData()
    }


    private fun updateLastBatchSection(lastBatch: KonveksiBatch?) {
        if (lastBatch != null) {
            binding.tvJumlahSesuai.text = "✔ ${lastBatch.jumlahSesuai} Sesuai"
            binding.tvJumlahTidakSesuai.text = "✖ ${lastBatch.jumlahTidakSesuai} Tidak Sesuai"
            binding.tvAkurasi.text = "Akurasi: ${lastBatch.akurasi}"
        } else {
            binding.tvJumlahSesuai.text = "✔ 0 Sesuai"
            binding.tvJumlahTidakSesuai.text = "✖ 0 Tidak Sesuai"
            binding.tvAkurasi.text = "Akurasi: 0%"
        }
    }

    private fun loadBatchData() {
        val allBatch = dbHelper.getAllBatch()
        adapter.update(allBatch)

        updateLastBatchSection(allBatch.firstOrNull())
    }


    private fun showEditDialog(profile: KonveksiProfile) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null)

        val etNama = dialogView.findViewById<EditText>(R.id.etEditNama)
        val etAlamat = dialogView.findViewById<EditText>(R.id.etEditAlamat)
        val etKontak = dialogView.findViewById<EditText>(R.id.etEditKontak)

        etNama.setText(profile.nama)
        etAlamat.setText(profile.alamat)
        etKontak.setText(profile.kontak)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Edit Profil Konveksi")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val updatedProfile = KonveksiProfile(
                    id = profile.id,
                    nama = etNama.text.toString().trim(),
                    alamat = etAlamat.text.toString().trim(),
                    kontak = etKontak.text.toString().trim()
                )
                if (dbHelper.updateProfile(updatedProfile)) {
                    Toast.makeText(requireContext(), "Profil diperbarui", Toast.LENGTH_SHORT).show()
                    updateUI()
                } else {
                    Toast.makeText(requireContext(), "Gagal memperbarui", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .create()

        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_blue))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_blue))
        }

        // ✅ PENTING! Tampilkan dialog-nya
        alertDialog.show()
    }

    private fun updateUI() {
        dbHelper.getProfile()?.let { profile ->
            binding.tvNamaKonveksi.text = "Nama Konveksi: ${profile.nama}"
            binding.tvAlamatKonveksi.text = "Alamat: ${profile.alamat}"
            binding.tvKontakKonveksi.text = "Kontak: ${profile.kontak}"
        } ?: run {
            binding.tvNamaKonveksi.text = "Nama Konveksi: -"
            binding.tvAlamatKonveksi.text = "Alamat: -"
            binding.tvKontakKonveksi.text = "Kontak: -"
        }
    }

    override fun onResume() {
        super.onResume()
        loadBatchData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
