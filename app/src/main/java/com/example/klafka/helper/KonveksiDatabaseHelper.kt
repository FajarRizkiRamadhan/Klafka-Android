package com.example.klafka.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.klafka.model.KonveksiBatch
import com.example.klafka.model.KonveksiProfile

//class KonveksiDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//
//    companion object {
//        const val DATABASE_NAME = "konveksi.db"
//        const val DATABASE_VERSION = 1
//        const val TABLE_NAME = "profil_konveksi"
//        const val COL_ID = "id"
//        const val COL_NAMA = "nama"
//        const val COL_ALAMAT = "alamat"
//        const val COL_KONTAK = "kontak"
//    }
//
//    override fun onCreate(db: SQLiteDatabase) {
//        val query = """
//            CREATE TABLE $TABLE_NAME (
//                $COL_ID INTEGER PRIMARY KEY,
//                $COL_NAMA TEXT,
//                $COL_ALAMAT TEXT,
//                $COL_KONTAK TEXT
//            )
//        """.trimIndent()
//        db.execSQL(query)
//
//        // Inisialisasi 1 data kosong
//        val initialInsert = "INSERT INTO $TABLE_NAME ($COL_ID, $COL_NAMA, $COL_ALAMAT, $COL_KONTAK) VALUES (1, '', '', '')"
//        db.execSQL(initialInsert)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
//        onCreate(db)
//    }
//
//    fun getProfile(): KonveksiProfile {
//        val db = readableDatabase
//        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COL_ID = 1", null)
//        var profile = KonveksiProfile(1, "", "", "")
//        if (cursor.moveToFirst()) {
//            profile = KonveksiProfile(
//                cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
//                cursor.getString(cursor.getColumnIndexOrThrow(COL_NAMA)),
//                cursor.getString(cursor.getColumnIndexOrThrow(COL_ALAMAT)),
//                cursor.getString(cursor.getColumnIndexOrThrow(COL_KONTAK))
//            )
//        }
//        cursor.close()
//        return profile
//    }
//
//    fun updateProfile(profile: KonveksiProfile): Boolean {
//        val db = writableDatabase
//        val values = ContentValues().apply {
//            put(COL_NAMA, profile.nama)
//            put(COL_ALAMAT, profile.alamat)
//            put(COL_KONTAK, profile.kontak)
//        }
//        val result = db.update(TABLE_NAME, values, "$COL_ID = ?", arrayOf("1"))
//        return result > 0
//    }
//
//
//}

class KonveksiDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "konveksi_klasifikasi.db"
        private const val DATABASE_VERSION = 1

        // Table Profil Konveksi
        const val TABLE_PROFIL = "profil_konveksi"
        const val COL_ID = "id"
        const val COL_NAMA = "nama"
        const val COL_ALAMAT = "alamat"
        const val COL_KONTAK = "kontak"

        // Table Batch Klasifikasi
        const val TABLE_BATCH = "batch_klasifikasi"
        const val COL_BATCH_ID = "id"
        const val COL_NAMA_JENIS_KAIN = "nama_jenis_kain"
        const val COL_JUMLAH_TOTAL = "jumlah_total"
        const val COL_JUMLAH_SESUAI = "jumlah_sesuai"
        const val COL_JUMLAH_TIDAK_SESUAI = "jumlah_tidak_sesuai"
        const val COL_AKURASI = "akurasi"
        const val COL_TANGGAL = "tanggal"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val queryProfil = """
            CREATE TABLE $TABLE_PROFIL (
                $COL_ID INTEGER PRIMARY KEY,
                $COL_NAMA TEXT,
                $COL_ALAMAT TEXT,
                $COL_KONTAK TEXT
            )
        """.trimIndent()

        val queryBatch = """
            CREATE TABLE $TABLE_BATCH (
                $COL_BATCH_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAMA_JENIS_KAIN TEXT,
                $COL_JUMLAH_TOTAL INTEGER,
                $COL_JUMLAH_SESUAI INTEGER,
                $COL_JUMLAH_TIDAK_SESUAI INTEGER,
                $COL_AKURASI TEXT,
                $COL_TANGGAL TEXT
            )
        """.trimIndent()

        db.execSQL(queryProfil)
        db.execSQL(queryBatch)

        // Insert default profil kosong
        val defaultInsert = "INSERT INTO $TABLE_PROFIL ($COL_ID, $COL_NAMA, $COL_ALAMAT, $COL_KONTAK) VALUES (1, '', '', '')"
        db.execSQL(defaultInsert)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROFIL")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BATCH")
        onCreate(db)
    }

    // ========== OPERASI PROFIL KONVEKSI ==========

    fun getProfile(): KonveksiProfile {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PROFIL WHERE $COL_ID = 1", null)
        var profile = KonveksiProfile(1, "", "", "")

        if (cursor.moveToFirst()) {
            profile = KonveksiProfile(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                nama = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAMA)),
                alamat = cursor.getString(cursor.getColumnIndexOrThrow(COL_ALAMAT)),
                kontak = cursor.getString(cursor.getColumnIndexOrThrow(COL_KONTAK))
            )
        }

        cursor.close()
        return profile
    }

    fun updateProfile(profile: KonveksiProfile): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NAMA, profile.nama)
            put(COL_ALAMAT, profile.alamat)
            put(COL_KONTAK, profile.kontak)
        }
        val result = db.update(TABLE_PROFIL, values, "$COL_ID = ?", arrayOf("1"))
        db.close()
        return result > 0
    }

    // ========== OPERASI BATCH KLASIFIKASI ==========

    fun insertBatch(batch: KonveksiBatch): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NAMA_JENIS_KAIN, batch.namaJenisKain)
            put(COL_JUMLAH_TOTAL, batch.jumlahTotal)
            put(COL_JUMLAH_SESUAI, batch.jumlahSesuai)
            put(COL_JUMLAH_TIDAK_SESUAI, batch.jumlahTidakSesuai)
            put(COL_AKURASI, batch.akurasi)
            put(COL_TANGGAL, batch.tanggal)
        }
        val result = db.insert(TABLE_BATCH, null, values)
        db.close()
        return result != -1L
    }
    fun deleteBatch(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_BATCH, "$COL_BATCH_ID = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }


    fun getAllBatch(): List<KonveksiBatch> {
        val db = readableDatabase
        val list = mutableListOf<KonveksiBatch>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_BATCH ORDER BY $COL_BATCH_ID DESC", null)

        if (cursor.moveToFirst()) {
            do {
                list.add(
                    KonveksiBatch(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BATCH_ID)),
                        namaJenisKain = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAMA_JENIS_KAIN)),
                        jumlahTotal = cursor.getInt(cursor.getColumnIndexOrThrow(COL_JUMLAH_TOTAL)),
                        jumlahSesuai = cursor.getInt(cursor.getColumnIndexOrThrow(COL_JUMLAH_SESUAI)),
                        jumlahTidakSesuai = cursor.getInt(cursor.getColumnIndexOrThrow(COL_JUMLAH_TIDAK_SESUAI)),
                        akurasi = cursor.getString(cursor.getColumnIndexOrThrow(COL_AKURASI)),
                        tanggal = cursor.getString(cursor.getColumnIndexOrThrow(COL_TANGGAL))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list
    }
}
