package pl.kozaps.movy.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import pl.kozaps.movy.domain.ActivityRepository
import pl.kozaps.movy.domain.ActivityTracker
import pl.kozaps.movy.domain.model.ActivityType

class MainViewModel(
    activityTracker: ActivityTracker,
    activityRepository: ActivityRepository
) : ViewModel() {

    init {
        activityTracker.observeActivity()
    }

    val currentActivity: StateFlow<ActivityType> = activityRepository
        .activityEvents
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ActivityType.STILL
        )

    val history: StateFlow<List<String>> = activityRepository.history
}
