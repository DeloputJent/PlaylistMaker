package com.practicum.playlistmaker.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.domain.api.ThemeRepository

class ThemeRepositoryImpl(context: Context): ThemeRepository {
    val settingsStorage: SharedPreferences = context.getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)

    override fun applyTheme(darkThemeEnabled: Boolean) {
        saveTheme(darkThemeEnabled)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    
    override fun saveTheme(themeEnabled: Boolean) {
            settingsStorage.edit()
                .putBoolean(NIGHT_THEME,themeEnabled)
                .apply()
    }

    override fun loadTheme(): Boolean {
        return settingsStorage.getBoolean(NIGHT_THEME, false)
    }

    companion object {
        const val NIGHT_THEME = "Night_mode_switcher"
        const val APP_SETTINGS = "Playlist_Maker_settings"
    }
}