package pl.kozaps.movy.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import pl.kozaps.movy.FakeActivityDao
import pl.kozaps.movy.domain.model.ActivityType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ActivityRepositoryTest {

    private val fakeDao = FakeActivityDao()
    // UnconfinedTestDispatcher sprawia, że launch wykonuje się natychmiastowo w teście
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = CoroutineScope(testDispatcher)

    @Test
    fun `emitActivity should insert record into dao`() = runTest {
        val repository = ActivityRepository(fakeDao, testScope)
        val type = ActivityType.WALKING
        
        repository.emitActivity(type, 100)

        val lastActivity = fakeDao.getLastActivity()
        assertEquals(type, lastActivity?.type)
        assertEquals(100, lastActivity?.confidence)
    }

    @Test
    fun `emitActivity should close previous activity if it exists`() = runTest {
        val repository = ActivityRepository(fakeDao, testScope)
        
        repository.emitActivity(ActivityType.STILL, 100)
        repository.emitActivity(ActivityType.WALKING, 100)
        
        val records = fakeDao.getAllActivities().first()
        val closedRecord = records.find { it.type == ActivityType.STILL }
        
        assertTrue(closedRecord?.endTime != null, "Previous activity should have an end time")
    }
}
