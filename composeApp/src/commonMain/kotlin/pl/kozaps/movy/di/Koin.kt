package pl.kozaps.movy.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(commonModule, platformModule)
    }

// iOS helper to handle KoinAppDeclaration mapping issues with Swift in older versions.
// Typically called from Kotlin in MainViewController or AppDelegate in KMP projects.
fun initKoin() = initKoin {}
