package pl.kozaps.movy.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import pl.kozaps.movy.ui.main.MainScreen
import pl.kozaps.movy.ui.statistics.StatisticsScreen

@Serializable
sealed interface Route {
    @Serializable
    data object Main : Route
    @Serializable
    data object Statistics : Route
}

private const val NAVIGATION_ANIMATION_DURATION = 600

@Composable
fun MovyNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Main,
        enterTransition = { fadeIn(animationSpec = tween(NAVIGATION_ANIMATION_DURATION)) },
        exitTransition = { fadeOut(animationSpec = tween(NAVIGATION_ANIMATION_DURATION)) },
        popEnterTransition = { fadeIn(animationSpec = tween(NAVIGATION_ANIMATION_DURATION)) },
        popExitTransition = { fadeOut(animationSpec = tween(NAVIGATION_ANIMATION_DURATION)) }
    ) {
        composable<Route.Main> {
            MainScreen(
                onStatsClick = { navController.navigate(Route.Statistics) }
            )
        }
        composable<Route.Statistics> {
            StatisticsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
