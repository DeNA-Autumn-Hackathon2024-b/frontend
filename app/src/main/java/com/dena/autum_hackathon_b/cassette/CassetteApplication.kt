package com.dena.autum_hackathon_b.cassette

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CassetteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant()
    }
}