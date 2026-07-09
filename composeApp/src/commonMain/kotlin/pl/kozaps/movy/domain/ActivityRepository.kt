package pl.kozaps.movy.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant
import pl.kozaps.movy.common.debugLog
import pl.kozaps.movy.common.getPlatformName
import pl.kozaps.movy.data.dao.ActivityDao
import pl.kozaps.movy.data.model.ActivityRecord
import pl.kozaps.movy.domain.model.ActivityType

class ActivityRepository(
    private val activityDao: ActivityDao
) {
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    private val _activityEvents = MutableSharedFlow<ActivityType>(extraBufferCapacity = 64)
    val activityEvents: SharedFlow<ActivityType> = _activityEvents.asSharedFlow()

    val history: StateFlow<List<String>> = activityDao.getAllActivities()
        .map { records ->
            records.take(20).map { record ->
                val localTime = record.startTime.toLocalDateTime(TimeZone.currentSystemDefault())
                val timestamp = "${localTime.hour.toString().padStart(2, '0')}:${localTime.minute.toString().padStart(2, '0')}:${localTime.second.toString().padStart(2, '0')}"
                "[$timestamp] ${record.type.name} (${record.confidence}%)"
            }
        }
        .stateIn(
            scope = repositoryScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    fun emitActivity(type: ActivityType, confidence: Int = 100) {
        _activityEvents.tryEmit(type)
        
        val now = Clock.System.now()
        
        // Logging
        debugLog("Tracker", "MovyLog: Emitting activity: $type ($confidence%)")
        
        repositoryScope.launch {
            handleNewActivity(type, confidence, now)
        }
    }

    private suspend fun handleNewActivity(type: ActivityType, confidence: Int, now: Instant) {
        val lastRecord = activityDao.getLastActivity()
        
        if (lastRecord != null && lastRecord.type == type && lastRecord.endTime == null) {
            // Same activity still ongoing, maybe update confidence if it's higher?
            // For now, let's just keep it as is.
            return
        }

        // Close previous activity
        if (lastRecord != null && lastRecord.endTime == null) {
            activityDao.update(lastRecord.copy(endTime = now))
        }

        // Start new activity
        val platform = getPlatformName() // We need to define this or pass it
        activityDao.insert(
            ActivityRecord(
                type = type,
                startTime = now,
                confidence = confidence,
                platform = platform
            )
        )
    }
}
