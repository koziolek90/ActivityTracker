package pl.kozaps.movy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant
import pl.kozaps.movy.domain.model.ActivityType

@Entity
data class ActivityRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: ActivityType,
    val startTime: Instant,
    val endTime: Instant? = null,
    val confidence: Int,
    val platform: String
)
