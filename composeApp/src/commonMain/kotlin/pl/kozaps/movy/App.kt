package pl.kozaps.movy

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pl.kozaps.movy.ui.navigation.MovyNavHost
import pl.kozaps.movy.ui.theme.MovyTheme

@Composable
@Preview
fun App() {
    MovyTheme {
        MovyNavHost()
    }
}
