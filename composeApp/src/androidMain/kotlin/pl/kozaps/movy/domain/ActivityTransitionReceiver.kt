package pl.kozaps.movy.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pl.kozaps.movy.domain.model.ActivityType

class ActivityTransitionReceiver : BroadcastReceiver(), KoinComponent {

    private val activityRepository: ActivityRepository by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)
            result?.transitionEvents?.lastOrNull()?.let { event ->
                val type = event.activityType.toActivityType()

                val pendingResult = goAsync()
                val job = activityRepository.emitActivity(type, 100)

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        job.join()
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
        }
    }

    private fun Int.toActivityType(): ActivityType = when (this) {
        DetectedActivity.STILL -> ActivityType.STILL
        DetectedActivity.WALKING -> ActivityType.WALKING
        DetectedActivity.RUNNING -> ActivityType.RUNNING
        DetectedActivity.ON_BICYCLE -> ActivityType.ON_BICYCLE
        DetectedActivity.IN_VEHICLE -> ActivityType.IN_VEHICLE
        else -> ActivityType.UNKNOWN
    }
}
