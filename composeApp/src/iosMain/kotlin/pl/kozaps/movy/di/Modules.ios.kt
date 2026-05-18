package pl.kozaps.movy.di

import org.koin.dsl.module
import pl.kozaps.movy.domain.ActivityTracker
import pl.kozaps.movy.domain.IosActivityTracker

actual val platformModule = module {
    single<ActivityTracker> { IosActivityTracker(get()) }
}
