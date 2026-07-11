package pl.kozaps.movy.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import pl.kozaps.movy.domain.usecase.GetDailyStatisticsUseCase
import pl.kozaps.movy.domain.usecase.StatisticsState
import kotlin.time.Clock

data class StatisticsUiState(
    val selectedDate: LocalDate,
    val statsState: StatisticsState = StatisticsState()
)

class StatisticsViewModel(
    private val getDailyStatisticsUseCase: GetDailyStatisticsUseCase
) : ViewModel() {

    private val tz = TimeZone.currentSystemDefault()
    private val today = Clock.System.now().toLocalDateTime(tz).date

    private val _uiState = MutableStateFlow(StatisticsUiState(selectedDate = today))
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    private var statsJob: Job? = null

    init {
        loadStatsForDate(today)
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        loadStatsForDate(date)
    }

    private fun loadStatsForDate(date: LocalDate) {
        statsJob?.cancel()
        statsJob = viewModelScope.launch {
            getDailyStatisticsUseCase(date).collect {
                _uiState.value = _uiState.value.copy(statsState = it)
            }
        }
    }
}
