package com.example.klafka.util

import android.content.Context
import android.os.Environment
import com.example.klafka.model.KonveksiBatch
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream

object PdfExporter {
    fun exportBatchListToPdf(context: Context, data: List<KonveksiBatch>): Boolean {
        return try {
            // 1) Buat dokumen PDF
            val doc = Document()
            // 2) Folder Download/app-specific
            val path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                ?: context.filesDir
            val file = File(path, "Laporan_Konveksi.pdf")
            PdfWriter.getInstance(doc, FileOutputStream(file))
            doc.open()

            // 3) Tambah judul
            val title = Paragraph("Laporan Klasifikasi Konveksi\n\n")
            title.alignment = Element.ALIGN_CENTER
            doc.add(title)

            // 4) Isi tabel sederhana
            data.forEach { b ->
                doc.add(Paragraph("Jenis Kain   : ${b.namaJenisKain}"))
                doc.add(Paragraph("Total        : ${b.jumlahTotal}"))
                doc.add(Paragraph("Sesuai       : ${b.jumlahSesuai}"))
                doc.add(Paragraph("Tidak Sesuai : ${b.jumlahTidakSesuai}"))
                doc.add(Paragraph("Akurasi      : ${b.akurasi}"))
                doc.add(Paragraph("Tanggal      : ${b.tanggal}\n"))
            }

            doc.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
