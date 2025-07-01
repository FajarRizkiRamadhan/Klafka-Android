package com.example.klafka.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.klafka.R
import com.example.klafka.databinding.ActivityMainBinding
import com.example.klafka.helper.DBHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationView.visibility =
                if (destination.id == R.id.kainDetailFragment) View.GONE
                else View.VISIBLE
        }

        // ✅ Dummy data ditambahkan DI DALAM onCreate
        insertDummyData()
    }
    fun showBottomNav(show: Boolean) {
        binding.bottomNavigationView.visibility = if (show) View.VISIBLE else View.GONE
    }


    private fun insertDummyData() {
        val db = DBHelper(this).writableDatabase

        val cursor = db.rawQuery("SELECT * FROM konveksi", null)
        if (cursor.count == 0) {
            db.execSQL("INSERT INTO konveksi (nama, alamat, kontak) VALUES ('Fajar', 'KP. Cilamkap', '08123456789')")
        }
        cursor.close()

        val cursor2 = db.rawQuery("SELECT * FROM riwayat", null)
        if (cursor2.count == 0) {
            db.execSQL("INSERT INTO riwayat (hasil, sesuai, tidak_sesuai, akurasi) VALUES ('Batch 1', 3, 1, 75.0)")
            db.execSQL("INSERT INTO riwayat (hasil, sesuai, tidak_sesuai, akurasi) VALUES ('Batch 2', 2, 2, 50.0)")
        }
        cursor2.close()
    }
}
