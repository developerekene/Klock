package com.devekene.klock.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorldClockDao {
    @Query("SELECT * FROM world_clocks")
    fun getAllWorldClocks(): Flow<List<WorldClockEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorldClock(clock: WorldClockEntity): Long

    @Query("DELETE FROM world_clocks WHERE id = :id")
    suspend fun deleteWorldClock(id: Long)
}
