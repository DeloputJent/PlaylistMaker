package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.domain.ThemeSettings
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor

class SettingsInteractorImpl(val repository: SettingsRepository): SettingsInteractor {

    override fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
    }
}