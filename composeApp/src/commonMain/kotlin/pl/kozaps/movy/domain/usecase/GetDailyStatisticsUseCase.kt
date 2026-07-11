package pl.kozaps.movy.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import pl.kozaps.movy.domain.ActivityRepository
import pl.kozaps.movy.domain.model.ActivityType

data class ActivityStats(
    val type: ActivityType,
    val totalDuration: Duration,
    val percentage: Float
)

data class StatisticsState(
    val stats: List<ActivityStats> = emptyList(),
    val totalActiveTime: Duration = 0.seconds
)

class GetDailyStatisticsUseCase(
    private val repository: ActivityRepository
) {
    operator fun invoke(date: LocalDate): Flow<StatisticsState> {
        val tz = TimeZone.currentSystemDefault()
        val startOfDay = date.atStartOfDayIn(tz)
        val endOfDay = date.plus(1, DateTimeUnit.DAY).atStartOfDayIn(tz)

        return repository.getActivitiesSince(startOfDay)
            .map { allRecords ->
                // Filter records to only include those that started before the end of the selected day
                val records = allRecords.filter { it.startTime < endOfDay }
                
                val now = Clock.System.now()
                val durations = mutableMapOf<ActivityType, Duration>()
                var totalDuration = 0.seconds

                records.forEach { record ->
                    // Constrain the activity interval to the selected day
                    val intervalStart = if (record.startTime < startOfDay) startOfDay else record.startTime
                    val intervalEnd = (record.endTime ?: now).let { if (it > endOfDay) endOfDay else it }
                    
                    if (intervalEnd > intervalStart) {
                        val duration = intervalEnd - intervalStart
                        durations[record.type] = (durations[record.type] ?: 0.seconds) + duration
                        if (record.type != ActivityType.STILL && record.type != ActivityType.UNKNOWN) {
                            totalDuration += duration
                        }
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
            .flowOn(Dispatchers.IO)
    }
}
