package com.example.klafka.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.klafka.helper.HistoryDatabaseHelper
import com.example.klafka.model.History

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = HistoryDatabaseHelper(application)

    private val _historyList = MutableLiveData<List<History>>()
    val historyList: LiveData<List<History>> get() = _historyList

    init {
        loadAllHistory()
    }

    fun loadAllHistory() {
        val all = dbHelper.getAllHistory()
        _historyList.postValue(all)
    }

    fun insertHistory(item: History) {
        dbHelper.insertHistory(item)
        loadAllHistory()
    }

    fun deleteHistoryById(id: Int) {
        if (dbHelper.deleteHistory(id)) {
            loadAllHistory()
        }
    }
}
