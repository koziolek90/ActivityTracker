package pl.kozaps.movy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import movy.composeapp.generated.resources.Res
import movy.composeapp.generated.resources.ic_bedtime
import movy.composeapp.generated.resources.ic_bike
import movy.composeapp.generated.resources.ic_car
import movy.composeapp.generated.resources.ic_history
import movy.composeapp.generated.resources.ic_question
import movy.composeapp.generated.resources.ic_run
import movy.composeapp.generated.resources.ic_walk
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import pl.kozaps.movy.domain.model.ActivityType

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel()
) {
    val activityType by viewModel.currentActivity.collectAsStateWithLifecycle()
    val history by viewModel.history.collectAsStateWithLifecycle()

    MainContent(
        activityType = activityType,
        history = history,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    activityType: ActivityType,
    history: List<String>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Movy Tracker",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            StatusCard(activityType)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_history),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Historia aktywności",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (history.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Czekam na pierwsze dane...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(history) { log ->
                            LogItem(log)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusCard(activityType: ActivityType) {
    val backgroundColor = when (activityType) {
        ActivityType.STILL -> MaterialTheme.colorScheme.secondaryContainer
        ActivityType.WALKING -> Color(0xFFE8F5E9)
        ActivityType.RUNNING -> Color(0xFFFFF3E0)
        ActivityType.ON_BICYCLE -> Color(0xFFE3F2FD)
        ActivityType.IN_VEHICLE -> Color(0xFFF3E5F5)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = when (activityType) {
        ActivityType.STILL -> MaterialTheme.colorScheme.onSecondaryContainer
        ActivityType.WALKING -> Color(0xFF2E7D32)
        ActivityType.RUNNING -> Color(0xFFE65100)
        ActivityType.ON_BICYCLE -> Color(0xFF1565C0)
        ActivityType.IN_VEHICLE -> Color(0xFF7B1FA2)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val icon = when (activityType) {
        ActivityType.STILL -> Res.drawable.ic_bedtime
        ActivityType.WALKING -> Res.drawable.ic_walk
        ActivityType.RUNNING -> Res.drawable.ic_run
        ActivityType.ON_BICYCLE -> Res.drawable.ic_bike
        ActivityType.IN_VEHICLE -> Res.drawable.ic_car
        else -> Res.drawable.ic_question
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = backgroundColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(contentColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = contentColor
                )
            }

            Spacer(Modifier.width(20.dp))

            Column {
                Text(
                    text = "Aktualny stan",
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor.copy(alpha = 0.7f)
                )
                Text(
                    text = activityType.toPolishName(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = contentColor
                )
            }
        }
    }
}

@Composable
private fun LogItem(log: String) {
    val parts = log.split("] ", limit = 2)
    val time = parts.getOrNull(0)?.removePrefix("[") ?: ""
    val message = parts.getOrNull(1) ?: log

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun ActivityType.toPolishName(): String = when (this) {
    ActivityType.STILL -> "Odpoczynek"
    ActivityType.WALKING -> "Spacer"
    ActivityType.RUNNING -> "Bieganie"
    ActivityType.ON_BICYCLE -> "Rower"
    ActivityType.IN_VEHICLE -> "W aucie / Autobusie"
    ActivityType.UNKNOWN -> "Nieznana"
}

@Preview
@Composable
private fun PreviewStatusCard() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatusCard(ActivityType.STILL)
            StatusCard(ActivityType.WALKING)
            StatusCard(ActivityType.RUNNING)
            StatusCard(ActivityType.ON_BICYCLE)
            StatusCard(ActivityType.IN_VEHICLE)
            StatusCard(ActivityType.UNKNOWN)
        }
    }
}
