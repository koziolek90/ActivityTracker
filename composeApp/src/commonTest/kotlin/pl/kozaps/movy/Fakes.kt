package pl.kozaps.movy

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant
import pl.kozaps.movy.data.dao.ActivityDao
import pl.kozaps.movy.data.model.ActivityRecord
import pl.kozaps.movy.domain.ActivityTracker

class FakeActivityDao : ActivityDao {
    private val records = MutableStateFlow<List<ActivityRecord>>(emptyList())

    override fun getAllActivities(): Flow<List<ActivityRecord>> = records

    override fun getActivitiesSince(since: Instant): Flow<List<ActivityRecord>> {
        return records.map { list -> list.filter { it.startTime >= since } }
    }

    override suspend fun getLastActivity(): ActivityRecord? {
        return records.value.firstOrNull()
    }

    override suspend fun insert(record: ActivityRecord) {
        records.value = listOf(record) + records.value
    }

    override suspend fun update(record: ActivityRecord) {
        records.value = records.value.map {
            if (it.startTime == record.startTime) record else it
        }
    }
}

class FakeActivityTracker : ActivityTracker {
    var observeActivityCalled = false
    override fun observeActivity() {
        observeActivityCalled = true
    }
}
