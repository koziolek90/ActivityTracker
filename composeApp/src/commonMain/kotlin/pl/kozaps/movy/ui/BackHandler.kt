package pl.kozaps.movy.ui

import androidx.compose.runtime.Composable

@Composable
expect fun MovyBackHandler(enabled: Boolean = true, onBack: () -> Unit)
