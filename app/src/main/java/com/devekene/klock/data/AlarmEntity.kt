package com.devekene.klock.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val hour: Int,
    val minute: Int,
    val label: String = "Alarm",
    val isEnabled: Boolean = true,
    val scheduleType: String = "ONCE",
    val repeatDays: String = "",
    val isGentleWake: Boolean = true,
    val soundUri: String = ""
)
