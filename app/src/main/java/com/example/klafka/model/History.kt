package com.example.klafka.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class History(
    val id: Int,
    val jenisKlasifikasi: String,
    val namaJenisKain: String,
    val tanggal: String,
    val imagePath: String,
    val imagePaths: List<String>? = null
) : Parcelable