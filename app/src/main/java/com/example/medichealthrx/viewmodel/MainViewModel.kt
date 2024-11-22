package com.example.medichealthrx.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.medichealthrx.data.database.AlarmDao
import com.example.medichealthrx.data.model.Alarm
import com.example.medichealthrx.worker.NotificationWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainViewModel(private val dao: AlarmDao, private val context: Context) : ViewModel() {

    private val _alarms = MutableStateFlow<List<Alarm>>(emptyList())
    val alarms: StateFlow<List<Alarm>> = _alarms.asStateFlow()

    init {
        loadAlarms()
    }

    private fun loadAlarms() {
        viewModelScope.launch {
            dao.getAll().collect { alarmList ->
                _alarms.value = alarmList
            }
        }
    }

    fun insert(alarm: Alarm) {
        viewModelScope.launch {
            dao.insertAlarm(alarm)
            scheduleNotification(alarm) // Programar la notificación
            loadAlarms()
        }
    }

    fun update(alarm: Alarm) {
        viewModelScope.launch {
            dao.updateAlarm(alarm)
            scheduleNotification(alarm) // Reprogramar la notificación
            loadAlarms()
        }
    }

    fun delete(alarm: Alarm) {
        viewModelScope.launch {
            dao.deleteAlarm(alarm)
            cancelNotification(alarm.alarmId) // Cancelar la notificación
        }
    }

    private fun scheduleNotification(alarm: Alarm) {
        val currentTime = System.currentTimeMillis()
        val delay = alarm.timeInMillis - currentTime

        if (delay > 0) {
            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        "alarmId" to alarm.alarmId,
                        "alarmName" to alarm.name,
                        "alarmTime" to "${alarm.hour}:${String.format("%02d", alarm.minute)} ${alarm.state}"
                    )
                )
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

    private fun cancelNotification(alarmId: Int) {
        WorkManager.getInstance(context).cancelAllWorkByTag(alarmId.toString())
    }

    fun toggleAlarm(alarm: Alarm) {
        val updatedAlarm = alarm.copy(checked = !alarm.checked)
        update(updatedAlarm)
    }

    fun getAlarmById(id: Int): Alarm? {
        return _alarms.value.find { it.alarmId == id }
    }
}
