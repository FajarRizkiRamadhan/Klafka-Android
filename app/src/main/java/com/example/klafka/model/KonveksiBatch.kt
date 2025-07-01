package com.example.klafka.model

data class KonveksiBatch(
    val id: Int,
    val namaJenisKain: String,
    val jumlahTotal: Int,
    val jumlahSesuai: Int,
    val jumlahTidakSesuai: Int,
    val akurasi: String,
    val tanggal: String
)
