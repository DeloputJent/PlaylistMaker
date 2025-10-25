package com.practicum.playlistmaker.data

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.domain.api.ThemeInteractor


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

    override fun switcherPosition(isChecked: Boolean): Boolean {
       return isChecked
    }
}