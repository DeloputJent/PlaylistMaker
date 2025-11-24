package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.ThemeSettings
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor

class SettingsInteractorImpl: SettingsInteractor {

    private var currentThemeSettings: ThemeSettings = ThemeSettings()

    override fun getThemeSettings(): ThemeSettings {
        return currentThemeSettings
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        currentThemeSettings = settings
    }
}