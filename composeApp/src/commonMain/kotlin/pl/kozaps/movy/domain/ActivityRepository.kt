package pl.kozaps.movy.domain

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import pl.kozaps.movy.common.debugLog
import pl.kozaps.movy.domain.model.ActivityType
import kotlin.time.Clock

class ActivityRepository {
    private val _activityEvents = MutableSharedFlow<ActivityType>(extraBufferCapacity = 64)
    val activityEvents: SharedFlow<ActivityType> = _activityEvents.asSharedFlow()

    // History for wireless debugging purposes
    private val _history = MutableStateFlow<List<String>>(emptyList())
    val history: StateFlow<List<String>> = _history.asStateFlow()

    fun emitActivity(type: ActivityType) {
        _activityEvents.tryEmit(type)
        
        // Explicitly use kotlinx.datetime to avoid conflicts with kotlin.time
        val now = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        val localTime = now.toLocalDateTime(timeZone)
        
        val timestamp = "${localTime.hour.toString().padStart(2, '0')}:${localTime.minute.toString().padStart(2, '0')}:${localTime.second.toString().padStart(2, '0')}"
        
        // Multiplatform logging using println
        debugLog("Tracker", "MovyLog: [$timestamp] Emitting activity: $type")
        
        val currentHistory = _history.value.toMutableList()
        currentHistory.add(0, "[$timestamp] ${type.name}") // Newest entries at the top
        _history.value = currentHistory.take(20) // Keep the 20 most recent entries
    }
}
