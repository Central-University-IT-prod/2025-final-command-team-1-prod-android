package ru.prod.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        val config = AppMetricaConfig.newConfigBuilder(API_KEY).build()
        // Initializing the AppMetrica SDK.
        AppMetrica.activate(this, config)
    }

    companion object {
        private const val API_KEY = "REDACTED"
    }
}