package com.practicum.playlistmaker.settings.data

import android.content.Context
import com.practicum.playlistmaker.settings.domain.ThemeSettings


interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}