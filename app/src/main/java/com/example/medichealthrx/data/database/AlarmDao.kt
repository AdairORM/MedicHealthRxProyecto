package com.example.medichealthrx.data.database


import androidx.room.*
import com.example.medichealthrx.data.model.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Insert
    suspend fun insertAlarm(alarm: Alarm)

    @Update
    suspend fun updateAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarm_database")
    fun getAll(): Flow<List<Alarm>>
}
