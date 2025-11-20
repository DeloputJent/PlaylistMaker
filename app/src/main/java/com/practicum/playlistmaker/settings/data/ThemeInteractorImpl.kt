package com.practicum.playlistmaker.settings.data

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.api.ThemeInteractor

class ThemeInteractorImpl: ThemeInteractor {

    override fun applyTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}