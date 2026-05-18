package pl.kozaps.movy

import androidx.compose.ui.window.ComposeUIViewController
import pl.kozaps.movy.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
