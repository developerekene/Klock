package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val hour: Int,
    val minute: Int,
    val label: String = "Alarm",
    val isEnabled: Boolean = true,
    val scheduleType: String = "ONCE", // ONCE, DAILY, WEEKDAYS, WEEKENDS, CUSTOM
    val repeatDays: String = "", // comma separated days if custom: "1,2,3,4,5"
    val isGentleWake: Boolean = true,
    val soundUri: String = ""
)
