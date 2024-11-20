package com.example.medichealthrx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medichealthrx.data.database.AlarmDao

class MainViewModelFactory(private val alarmDao: AlarmDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(alarmDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
