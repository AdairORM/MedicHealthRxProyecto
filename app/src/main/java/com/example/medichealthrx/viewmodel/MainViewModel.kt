package com.example.medichealthrx.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medichealthrx.data.database.AlarmDao
import com.example.medichealthrx.data.model.Alarm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val dao: AlarmDao) : ViewModel() {

    // Estado de las alarmas que observa la UI
    private val _alarms = MutableStateFlow<List<Alarm>>(emptyList())
    val alarms: StateFlow<List<Alarm>> = _alarms.asStateFlow()

    init {
        loadAlarms()
    }

    // Cargar todas las alarmas desde la base de datos
    private fun loadAlarms() {
        viewModelScope.launch {
            dao.getAll().collect { alarmList ->
                _alarms.value = alarmList
            }
        }
    }

    // Insertar una nueva alarma
    fun insert(alarm: Alarm) {
        viewModelScope.launch {
            dao.insertAlarm(alarm)
            loadAlarms() // Actualiza el estado después de insertar
        }
    }

    // Actualizar una alarma existente
    fun update(alarm: Alarm) {
        viewModelScope.launch {
            dao.updateAlarm(alarm)
            loadAlarms() // Actualiza el estado después de actualizar
        }
    }

    // Eliminar una alarma
    fun delete(alarm: Alarm) {
        viewModelScope.launch {
            dao.deleteAlarm(alarm)
            loadAlarms() // Actualiza el estado después de eliminar
        }
    }

    // Alternar el estado de activación de una alarma
    fun toggleAlarm(alarm: Alarm) {
        val updatedAlarm = alarm.copy(checked = !alarm.checked)
        update(updatedAlarm)
    }

    // Buscar una alarma por ID
    fun getAlarmById(id: Int): Alarm? {
        return _alarms.value.find { it.alarmId == id }
    }
}