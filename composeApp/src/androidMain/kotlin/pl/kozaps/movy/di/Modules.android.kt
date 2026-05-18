package pl.kozaps.movy.di

import org.koin.dsl.module
import pl.kozaps.movy.domain.ActivityTracker
import pl.kozaps.movy.domain.AndroidActivityTracker

actual val platformModule = module {
    single<ActivityTracker> { AndroidActivityTracker(get()) }
}
