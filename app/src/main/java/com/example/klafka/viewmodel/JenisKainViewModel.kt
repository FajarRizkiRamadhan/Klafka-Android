package com.example.klafka.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.klafka.model.JenisKain
import com.example.klafka.repository.JenisKainRepository

class JenisKainViewModel : ViewModel() {

    private val _kain = MutableLiveData<JenisKain?>() // ← Nullable!
    val kain: LiveData<JenisKain?> get() = _kain

    fun loadKainByName(nama: String) {
        val result = JenisKainRepository.findKainByName(nama)
        _kain.value = result
    }
}
