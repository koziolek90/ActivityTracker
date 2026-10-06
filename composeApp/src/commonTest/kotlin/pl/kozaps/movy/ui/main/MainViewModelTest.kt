package pl.kozaps.movy.ui.main

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pl.kozaps.movy.FakeActivityDao
import pl.kozaps.movy.FakeActivityTracker
import pl.kozaps.movy.domain.ActivityRepository
import pl.kozaps.movy.domain.model.ActivityType
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = CoroutineScope(testDispatcher)
    private val fakeDao = FakeActivityDao()
    private val fakeTracker = FakeActivityTracker()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel should call observeActivity on init`() = runTest {
        val repository = ActivityRepository(fakeDao, testScope)
        MainViewModel(fakeTracker, repository)
        assertTrue(fakeTracker.observeActivityCalled)
    }

    @Test
    fun `currentActivity should reflect emitted activity`() = runTest {
        val repository = ActivityRepository(fakeDao, testScope)
        val viewModel = MainViewModel(fakeTracker, repository)
        
        val collectJob = launch(testDispatcher) {
            viewModel.currentActivity.collect {}
        }

        repository.emitActivity(ActivityType.RUNNING)

        assertEquals(ActivityType.RUNNING, viewModel.currentActivity.value)
        collectJob.cancel()
    }
}
