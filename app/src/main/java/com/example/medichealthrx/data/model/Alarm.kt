package com.example.medichealthrx.data.model


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "alarm_database")
data class Alarm(
    @PrimaryKey(autoGenerate = true) val alarmId: Int = 0,
    @ColumnInfo(name = "hour") var hour: Int = 0,
    @ColumnInfo(name = "minute") var minute: Int = 0,
    @ColumnInfo(name = "state") var state: String = "AM",
    @ColumnInfo(name = "checked") var checked: Boolean = false,
    @ColumnInfo(name = "timeInMillis") var timeInMillis: Long,
    @ColumnInfo(name = "name") var name: String = ""
) : Parcelable
