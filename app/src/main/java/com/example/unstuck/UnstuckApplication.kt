package com.example.unstuck

import android.app.Application
import com.example.unstuck.ui.screens.settings.SettingsManager
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject

@HiltAndroidApp
class UnstuckApplication(): Application() {
    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onCreate() {
        super.onCreate()
    }
}