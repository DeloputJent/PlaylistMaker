package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val NIGHT_THEME = "Night_mode_switcher"
const val APP_SETTINGS = "Playlist_Maker_settings"


class App : Application() {

    private var darkTheme = false
    lateinit var settingsStorage: SharedPreferences

    fun checktheme():Boolean{return darkTheme}

    override fun onCreate() {
        super.onCreate()
        settingsStorage = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)
        darkTheme=settingsStorage.getBoolean(NIGHT_THEME, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme=darkThemeEnabled
        settingsStorage.edit()
            .putBoolean(NIGHT_THEME,darkThemeEnabled)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}