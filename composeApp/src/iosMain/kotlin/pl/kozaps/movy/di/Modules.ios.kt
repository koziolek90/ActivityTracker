package pl.kozaps.movy.di

import org.koin.dsl.module
import pl.kozaps.movy.domain.ActivityTracker
import pl.kozaps.movy.domain.IosActivityTracker

import pl.kozaps.movy.data.db.getDatabaseBuilder

actual val platformModule = module {
    single<ActivityTracker> { IosActivityTracker(get()) }
    single { getDatabaseBuilder() }
}
