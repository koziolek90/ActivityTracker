package pl.kozaps.movy.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pl.kozaps.movy.data.db.AppDatabase
import pl.kozaps.movy.data.db.getRoomDatabase
import pl.kozaps.movy.domain.ActivityRepository
import pl.kozaps.movy.domain.usecase.GetDailyStatisticsUseCase
import pl.kozaps.movy.ui.main.MainViewModel
import pl.kozaps.movy.ui.statistics.StatisticsViewModel

expect val platformModule: Module

val commonModule = module {
    single { ActivityRepository(get()) }
    factory { GetDailyStatisticsUseCase(get()) }

    viewModel { MainViewModel(get(), get()) }
    viewModel { StatisticsViewModel(get()) }
    
    single<AppDatabase> { getRoomDatabase(get()) }
    single { get<AppDatabase>().activityDao() }
}
