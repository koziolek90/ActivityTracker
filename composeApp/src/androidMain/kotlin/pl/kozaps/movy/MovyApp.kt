package pl.kozaps.movy

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import pl.kozaps.movy.di.initKoin

class MovyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@MovyApp)
        }
    }
}
