package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class KlockViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = KlockRepository(database.alarmDao(), database.worldClockDao(), database.reminderDao())

    val alarms: StateFlow<List<AlarmEntity>> = repository.allAlarms
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val worldClocks: StateFlow<List<WorldClockEntity>> = repository.allWorldClocks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reminders: StateFlow<List<ReminderEntity>> = repository.allReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Stopwatch State
    private val _stopwatchTime = MutableStateFlow(0L)
    val stopwatchTime: StateFlow<Long> = _stopwatchTime.asStateFlow()

    private val _isStopwatchRunning = MutableStateFlow(false)
    val isStopwatchRunning: StateFlow<Boolean> = _isStopwatchRunning.asStateFlow()

    private val _laps = MutableStateFlow<List<Long>>(emptyList())
    val laps: StateFlow<List<Long>> = _laps.asStateFlow()

    private var stopwatchJob: Job? = null

    fun startStopwatch() {
        if (_isStopwatchRunning.value) return
        _isStopwatchRunning.value = true
        stopwatchJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis() - _stopwatchTime.value
            while (_isStopwatchRunning.value) {
                _stopwatchTime.value = System.currentTimeMillis() - startTime
                delay(10)
            }
        }
    }

    fun pauseStopwatch() {
        _isStopwatchRunning.value = false
        stopwatchJob?.cancel()
    }

    fun resetStopwatch() {
        pauseStopwatch()
        _stopwatchTime.value = 0L
        _laps.value = emptyList()
    }

    fun lapStopwatch() {
        if (!_isStopwatchRunning.value) return
        val current = _stopwatchTime.value
        _laps.value = listOf(current) + _laps.value
    }

    // Timer State
    private val _timerDuration = MutableStateFlow(0L)
    val timerDuration: StateFlow<Long> = _timerDuration.asStateFlow()

    private val _timerRemaining = MutableStateFlow(0L)
    val timerRemaining: StateFlow<Long> = _timerRemaining.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private var timerJob: Job? = null

    fun startTimer(durationMillis: Long) {
        if (durationMillis > 0L) {
            _timerDuration.value = durationMillis
            _timerRemaining.value = durationMillis
        }
        if (_timerRemaining.value <= 0L) return
        _isTimerRunning.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerRemaining.value > 0 && _isTimerRunning.value) {
                delay(1000)
                _timerRemaining.value = (_timerRemaining.value - 1000).coerceAtLeast(0L)
            }
            if (_timerRemaining.value == 0L) {
                _isTimerRunning.value = false
            }
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
    }

    fun resumeTimer() {
        if (_timerRemaining.value > 0) {
            startTimer(0L)
        }
    }

    fun resetTimer() {
        pauseTimer()
        _timerRemaining.value = _timerDuration.value
    }

    // Alarm Actions
    fun addAlarm(alarm: AlarmEntity) {
        viewModelScope.launch {
            repository.insertAlarm(alarm)
        }
    }

    fun updateAlarm(alarm: AlarmEntity) {
        viewModelScope.launch {
            repository.updateAlarm(alarm)
        }
    }

    fun deleteAlarm(id: Long) {
        viewModelScope.launch {
            repository.deleteAlarm(id)
        }
    }

    // World Clock Actions
    fun addWorldClock(cityName: String, timezoneId: String, country: String) {
        viewModelScope.launch {
            repository.insertWorldClock(WorldClockEntity(cityName = cityName, timezoneId = timezoneId, country = country))
        }
    }

    fun deleteWorldClock(id: Long) {
        viewModelScope.launch {
            repository.deleteWorldClock(id)
        }
    }

    // Reminder Actions
    fun addReminder(title: String, timestamp: Long) {
        viewModelScope.launch {
            repository.insertReminder(ReminderEntity(title = title, timestamp = timestamp))
        }
    }

    fun toggleReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.updateReminder(reminder.copy(isCompleted = !reminder.isCompleted))
        }
    }

    fun deleteReminder(id: Long) {
        viewModelScope.launch {
            repository.deleteReminder(id)
        }
    }
}
