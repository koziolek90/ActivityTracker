package pl.kozaps.movy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant
import pl.kozaps.movy.domain.ActivityRepository
import pl.kozaps.movy.domain.model.ActivityType
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class ActivityStats(
    val type: ActivityType,
    val totalDuration: Duration,
    val percentage: Float
)

data class StatisticsState(
    val stats: List<ActivityStats> = emptyList(),
    val totalActiveTime: Duration = 0.seconds
)

class StatisticsViewModel(
    private val repository: ActivityRepository
) : ViewModel() {

    val state: StateFlow<StatisticsState> = repository.getActivitiesSince(getStartOfDay())
        .map { records ->
            val now = Clock.System.now()
            val durations = mutableMapOf<ActivityType, Duration>()
            var totalDuration = 0.seconds

            records.forEach { record ->
                val end = record.endTime ?: now
                val duration = end - record.startTime
                durations[record.type] = (durations[record.type] ?: 0.seconds) + duration
                if (record.type != ActivityType.STILL && record.type != ActivityType.UNKNOWN) {
                    totalDuration += duration
                }
            }

            val statsList = durations.map { (type, duration) ->
                val percentage = if (totalDuration > 0.seconds && type != ActivityType.STILL && type != ActivityType.UNKNOWN) {
                    (duration.inWholeSeconds.toFloat() / totalDuration.inWholeSeconds.toFloat())
                } else 0f
                ActivityStats(type, duration, percentage)
            }.sortedByDescending { it.totalDuration }

            StatisticsState(statsList, totalDuration)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StatisticsState()
        )

    private fun getStartOfDay(): Instant {
        val tz = TimeZone.currentSystemDefault()
        return Clock.System.now().toLocalDateTime(tz).date.atStartOfDayIn(tz)
    }
}
