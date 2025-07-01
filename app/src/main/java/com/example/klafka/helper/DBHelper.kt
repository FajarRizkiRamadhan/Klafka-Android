package com.example.klafka.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "klafka.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE konveksi (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nama TEXT,
                alamat TEXT,
                kontak TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE riwayat (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                hasil TEXT,
                sesuai INTEGER,
                tidak_sesuai INTEGER,
                akurasi REAL
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS konveksi")
        db.execSQL("DROP TABLE IF EXISTS riwayat")
        onCreate(db)
    }
}
