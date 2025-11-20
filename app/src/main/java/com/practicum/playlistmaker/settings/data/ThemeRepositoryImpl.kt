package com.practicum.playlistmaker.settings.data

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.domain.api.ThemeRepository

class ThemeRepositoryImpl(context: Context): ThemeRepository {
    val settingsStorage: SharedPreferences = context.getSharedPreferences(APP_SETTINGS,
        Context.MODE_PRIVATE
    )

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