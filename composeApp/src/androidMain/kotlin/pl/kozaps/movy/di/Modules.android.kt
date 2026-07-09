package pl.kozaps.movy.di

import org.koin.dsl.module
import pl.kozaps.movy.domain.ActivityTracker
import pl.kozaps.movy.domain.AndroidActivityTracker

import pl.kozaps.movy.data.db.getDatabaseBuilder
import pl.kozaps.movy.data.db.getRoomDatabase

actual val platformModule = module {
    single<ActivityTracker> { AndroidActivityTracker(get()) }
    single { getDatabaseBuilder(get()) }
}
