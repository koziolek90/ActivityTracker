package pl.kozaps.movy.domain

interface ActivityTracker {
    /**
     * Initializes activity monitoring.
     * Results will be delivered to ActivityRepository.
     */
    fun observeActivity()
}
