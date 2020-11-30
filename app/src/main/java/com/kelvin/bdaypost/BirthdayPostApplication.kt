package com.kelvin.bdaypost

import android.app.Application
import timber.log.Timber

class BirthdayPostApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}