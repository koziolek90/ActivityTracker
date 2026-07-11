package pl.kozaps.movy

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import pl.kozaps.movy.ui.main.MainScreen
import pl.kozaps.movy.ui.theme.MovyTheme
import pl.kozaps.movy.ui.statistics.StatisticsScreen
import pl.kozaps.movy.ui.common.MovyBackHandler

enum class Screen {
    Main, Stats
}

@Composable
@Preview
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.Main) }
    
    MovyTheme {
        when (currentScreen) {
            Screen.Main -> MainScreen(
                onStatsClick = { currentScreen = Screen.Stats }
            )
            Screen.Stats -> {
                MovyBackHandler(onBack = { currentScreen = Screen.Main })
                StatisticsScreen(
                    onBackClick = { currentScreen = Screen.Main }
                )
            }
        }
    }
}
