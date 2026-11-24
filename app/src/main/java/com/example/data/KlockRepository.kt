package com.example.data

import kotlinx.coroutines.flow.Flow

class KlockRepository(
    private val alarmDao: AlarmDao,
    private val worldClockDao: WorldClockDao,
    private val reminderDao: ReminderDao
) {
    val allAlarms: Flow<List<AlarmEntity>> = alarmDao.getAllAlarms()
    val allWorldClocks: Flow<List<WorldClockEntity>> = worldClockDao.getAllWorldClocks()
    val allReminders: Flow<List<ReminderEntity>> = reminderDao.getAllReminders()

    suspend fun insertAlarm(alarm: AlarmEntity) = alarmDao.insertAlarm(alarm)
    suspend fun updateAlarm(alarm: AlarmEntity) = alarmDao.updateAlarm(alarm)
    suspend fun deleteAlarm(id: Long) = alarmDao.deleteAlarm(id)

    suspend fun insertWorldClock(clock: WorldClockEntity) = worldClockDao.insertWorldClock(clock)
    suspend fun deleteWorldClock(id: Long) = worldClockDao.deleteWorldClock(id)

    suspend fun insertReminder(reminder: ReminderEntity) = reminderDao.insertReminder(reminder)
    suspend fun updateReminder(reminder: ReminderEntity) = reminderDao.updateReminder(reminder)
    suspend fun deleteReminder(id: Long) = reminderDao.deleteReminder(id)
}
