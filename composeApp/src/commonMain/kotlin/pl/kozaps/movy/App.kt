package pl.kozaps.movy

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pl.kozaps.movy.ui.MainScreen
import pl.kozaps.movy.ui.MovyTheme

@Composable
@Preview
fun App() {
    MovyTheme {
        MainScreen()
    }
}
