package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        val settingsRepository = SettingsRepositoryImpl(this)
        val themeSettings = settingsRepository.getThemeSettings()


        AppCompatDelegate.setDefaultNightMode(
            if (themeSettings.darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}