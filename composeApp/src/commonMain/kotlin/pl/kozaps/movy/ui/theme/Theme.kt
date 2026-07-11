package pl.kozaps.movy.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ActivityColors(
    val walkingBg: Color,
    val walkingContent: Color,
    val runningBg: Color,
    val runningContent: Color,
    val bicycleBg: Color,
    val bicycleContent: Color,
    val vehicleBg: Color,
    val vehicleContent: Color
)

private val LightActivityColors = ActivityColors(
    walkingBg = Color(0xFFE8F5E9),
    walkingContent = Color(0xFF2E7D32),
    runningBg = Color(0xFFFFF3E0),
    runningContent = Color(0xFFE65100),
    bicycleBg = Color(0xFFE3F2FD),
    bicycleContent = Color(0xFF1565C0),
    vehicleBg = Color(0xFFF3E5F5),
    vehicleContent = Color(0xFF7B1FA2)
)

private val DarkActivityColors = ActivityColors(
    walkingBg = Color(0xFF1B3320),
    walkingContent = Color(0xFF81C784),
    runningBg = Color(0xFF3E2723),
    runningContent = Color(0xFFFFB74D),
    bicycleBg = Color(0xFF0D47A1).copy(alpha = 0.3f),
    bicycleContent = Color(0xFF64B5F6),
    vehicleBg = Color(0xFF311B92).copy(alpha = 0.3f),
    vehicleContent = Color(0xFFB39DDB)
)

val LocalActivityColors = staticCompositionLocalOf { LightActivityColors }

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00E5FF),
    secondary = Color(0xFFD500F9),
    tertiary = Color(0xFF00B0FF),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    secondaryContainer = Color(0xFF2C2C2C),
    onSecondaryContainer = Color(0xFFE0E0E0)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1565C0),
    secondary = Color(0xFF7B1FA2),
    tertiary = Color(0xFF0288D1),
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    secondaryContainer = Color(0xFFE3F2FD),
    onSecondaryContainer = Color(0xFF0D47A1)
)

@Composable
fun MovyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val activityColors = if (darkTheme) DarkActivityColors else LightActivityColors

    CompositionLocalProvider(
        LocalActivityColors provides activityColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
