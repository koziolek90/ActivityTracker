package pl.kozaps.movy.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun MovyBackHandler(enabled: Boolean, onBack: () -> Unit) {
    Log.d("Movy", "MovyBackHandler: enabled=$enabled")
    BackHandler(enabled) {
        Log.d("Movy", "MovyBackHandler: onBack triggered")
        onBack()
    }
}
