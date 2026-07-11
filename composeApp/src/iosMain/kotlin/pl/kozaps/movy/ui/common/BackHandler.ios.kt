package pl.kozaps.movy.ui.common

import androidx.compose.runtime.Composable

@Composable
actual fun MovyBackHandler(enabled: Boolean, onBack: () -> Unit) {
    // No-op for iOS as it doesn't have a hardware back button
}
