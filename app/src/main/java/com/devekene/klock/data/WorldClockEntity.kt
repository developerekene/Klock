package com.devekene.klock.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "world_clocks")
data class WorldClockEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cityName: String,
    val timezoneId: String,
    val country: String
)
