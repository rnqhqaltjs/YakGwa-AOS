package com.prography.yakgwa

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class YakGwaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}