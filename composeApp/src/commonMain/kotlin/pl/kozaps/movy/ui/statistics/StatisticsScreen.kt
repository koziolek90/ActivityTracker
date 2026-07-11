package pl.kozaps.movy.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import pl.kozaps.movy.domain.model.ActivityType
import pl.kozaps.movy.domain.usecase.ActivityStats
import pl.kozaps.movy.domain.usecase.StatisticsState
import pl.kozaps.movy.ui.common.MovyBackHandler
import pl.kozaps.movy.ui.theme.MovyTheme
import pl.kozaps.movy.ui.theme.LocalActivityColors
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@Composable
fun StatisticsScreen(
    onBackClick: () -> Unit,
    viewModel: StatisticsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MovyBackHandler(onBack = onBackClick)

    StatisticsContent(
        state = state,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsContent(
    state: StatisticsState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Statystyki dnia", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                TotalActiveTimeCard(state.totalActiveTime)
            }

            item {
                Text(
                    "Podział aktywności",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            if (state.stats.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Brak danych z dzisiaj", color = MaterialTheme.colorScheme.outline)
                    }
                }
            } else {
                items(state.stats) { stat ->
                    ActivityStatItem(stat)
                }
            }
        }
    }
}

@Composable
fun TotalActiveTimeCard(totalTime: Duration) {
    val hours = totalTime.inWholeHours
    val minutes = totalTime.inWholeMinutes % 60
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Łączny czas w ruchu", style = MaterialTheme.typography.labelLarge)
            Text(
                text = "${hours}h ${minutes}m",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun ActivityStatItem(stat: ActivityStats) {
    val activityColors = LocalActivityColors.current
    val color = when (stat.type) {
        ActivityType.STILL -> MaterialTheme.colorScheme.outline
        ActivityType.WALKING -> activityColors.walkingContent
        ActivityType.RUNNING -> activityColors.runningContent
        ActivityType.ON_BICYCLE -> activityColors.bicycleContent
        ActivityType.IN_VEHICLE -> activityColors.vehicleContent
        else -> MaterialTheme.colorScheme.outline
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(12.dp).background(color, CircleShape))
            Text(
                stat.type.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                formatDuration(stat.totalDuration),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        if (stat.percentage > 0) {
            LinearProgressIndicator(
                progress = { stat.percentage },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = color,
                trackColor = color.copy(alpha = 0.1f)
            )
        }
    }
}

private fun formatDuration(duration: Duration): String {
    val h = duration.inWholeHours
    val m = duration.inWholeMinutes % 60
    val s = duration.inWholeSeconds % 60
    return when {
        h > 0 -> "${h}h ${m}m"
        m > 0 -> "${m}m ${s}s"
        else -> "${s}s"
    }
}

@Preview(name = "Statistics - Light")
@Preview(name = "Statistics - Dark", showBackground = true, uiMode = 32)
@Composable
private fun PreviewStatisticsContent() {
    MovyTheme {
        StatisticsContent(
            state = StatisticsState(
                totalActiveTime = 2.hours + 45.minutes,
                stats = listOf(
                    ActivityStats(ActivityType.WALKING, 1.hours + 30.minutes, 0.6f),
                    ActivityStats(ActivityType.ON_BICYCLE, 45.minutes, 0.3f),
                    ActivityStats(ActivityType.RUNNING, 30.minutes, 0.1f)
                )
            ),
            onBackClick = {}
        )
    }
}
