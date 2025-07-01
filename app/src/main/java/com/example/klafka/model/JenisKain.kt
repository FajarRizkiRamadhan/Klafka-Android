package com.example.klafka.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JenisKain(
    val nama: String,
    val deskripsi: String,
    val kegunaan: String,
    val karakteristik: String,
    val perawatan: String,
    val gambarResId: Int
): Parcelable