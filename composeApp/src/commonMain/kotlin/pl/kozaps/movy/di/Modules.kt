package pl.kozaps.movy.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    single { ActivityRepository(get(), get()) }
    factory { GetDailyStatisticsUseCase(get()) }

    viewModel { MainViewModel(get(), get()) }
    viewModel { StatisticsViewModel(get()) }
    
    single<AppDatabase> { getRoomDatabase(get()) }
    single { get<AppDatabase>().activityDao() }
}
