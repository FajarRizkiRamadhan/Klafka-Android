package com.example.klafka.util

import android.content.Context
import android.os.Environment
import com.example.klafka.model.KonveksiBatch
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

object ExcelExporter {
    fun exportBatchListToExcel(context: Context, data: List<KonveksiBatch>): Boolean {
        return try {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Batch Konveksi")
            // header
            val header = sheet.createRow(0)
            listOf("Jenis Kain","Total","Sesuai","Tidak Sesuai","Akurasi","Tanggal")
                .forEachIndexed { i, title -> header.createCell(i).setCellValue(title) }
            // isi
            data.forEachIndexed { r, b ->
                val row = sheet.createRow(r+1)
                row.createCell(0).setCellValue(b.namaJenisKain)
                row.createCell(1).setCellValue(b.jumlahTotal.toDouble())
                row.createCell(2).setCellValue(b.jumlahSesuai.toDouble())
                row.createCell(3).setCellValue(b.jumlahTidakSesuai.toDouble())
                row.createCell(4).setCellValue(b.akurasi)
                row.createCell(5).setCellValue(b.tanggal)
            }
            // simpan
            val path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                ?: context.filesDir
            val file = File(path, "Laporan_Konveksi.xlsx")
            workbook.write(FileOutputStream(file))
            workbook.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
