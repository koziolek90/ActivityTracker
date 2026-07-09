package pl.kozaps.movy.domain

import platform.CoreMotion.CMMotionActivityManager
import platform.Foundation.NSOperationQueue
import pl.kozaps.movy.domain.model.ActivityType
import platform.CoreMotion.CMMotionActivity

class IosActivityTracker(
    private val activityRepository: ActivityRepository
) : ActivityTracker {
    private val motionManager = CMMotionActivityManager()

    override fun observeActivity() {
        if (!CMMotionActivityManager.isActivityAvailable()) {
            return
        }

        motionManager.startActivityUpdatesToQueue(NSOperationQueue.mainQueue) { activity ->
            activity?.let {
                val confidence = when (it.confidence) {
                    platform.CoreMotion.CMMotionActivityConfidenceLow -> 33
                    platform.CoreMotion.CMMotionActivityConfidenceMedium -> 66
                    platform.CoreMotion.CMMotionActivityConfidenceHigh -> 100
                    else -> 0
                }
                activityRepository.emitActivity(it.toActivityType(), confidence)
            }
        }
    }

    private fun CMMotionActivity.toActivityType(): ActivityType {
        return when {
            automotive -> ActivityType.IN_VEHICLE
            cycling -> ActivityType.ON_BICYCLE
            walking -> ActivityType.WALKING
            running -> ActivityType.RUNNING
            stationary -> ActivityType.STILL
            else -> ActivityType.UNKNOWN
        }
    }
}
