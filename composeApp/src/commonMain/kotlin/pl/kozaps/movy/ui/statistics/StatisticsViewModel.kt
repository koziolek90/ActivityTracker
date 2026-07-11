package pl.kozaps.movy.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.kozaps.movy.domain.usecase.GetDailyStatisticsUseCase
import pl.kozaps.movy.domain.usecase.StatisticsState

class StatisticsViewModel(
    getDailyStatisticsUseCase: GetDailyStatisticsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticsState())
    val state: StateFlow<StatisticsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getDailyStatisticsUseCase().collect {
                _state.value = it
            }
        }
    }
}
