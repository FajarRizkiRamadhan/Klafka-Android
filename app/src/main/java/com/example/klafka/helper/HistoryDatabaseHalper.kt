package com.example.klafka.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.klafka.model.History

class HistoryDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "klafka_history.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "history"
        const val COLUMN_ID = "id"
        const val COLUMN_JENIS_KLASIFIKASI = "jenis_klasifikasi"
        const val COLUMN_NAMA_JENIS_KAIN = "nama_jenis_kain"
        const val COLUMN_TANGGAL = "tanggal"
        const val COLUMN_IMAGE_PATH = "image_path"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_JENIS_KLASIFIKASI TEXT,
                $COLUMN_NAMA_JENIS_KAIN TEXT,
                $COLUMN_TANGGAL TEXT,
                $COLUMN_IMAGE_PATH TEXT
            )
        """.trimIndent()

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertHistory(history: History): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_JENIS_KLASIFIKASI, history.jenisKlasifikasi)
            put(COLUMN_NAMA_JENIS_KAIN, history.namaJenisKain)
            put(COLUMN_TANGGAL, history.tanggal)
            put(COLUMN_IMAGE_PATH, history.imagePath)
        }


        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }

    fun getAllHistory(): List<History> {
        val historyList = mutableListOf<History>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val id =cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val jenisKlasifikasi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JENIS_KLASIFIKASI))
                val namaJenisKain = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_JENIS_KAIN))
                val tanggal = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL))
                val imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH))

                val history = History(
                    id = id,
                    jenisKlasifikasi = jenisKlasifikasi,
                    namaJenisKain = namaJenisKain,
                    tanggal = tanggal,
                    imagePath = imagePath
                )

                historyList.add(history)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return historyList
    }

    fun deleteHistory(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }


}
