package com.practicum.playlistmaker.settings.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.settings.domain.ThemeSettings
import androidx.core.content.edit

class SettingsRepositoryImpl(context: Context) : SettingsRepository{

    private val gson = Gson()
    val settingsStorage: SharedPreferences = context.getSharedPreferences(APP_SETTINGS,
        Context.MODE_PRIVATE
    )

    override fun getThemeSettings(): ThemeSettings {
        val json = settingsStorage.getString(APP_SETTINGS, null)
        if (json == null) {
            return defaultTheme
        } else {
            return gson.fromJson(json, ThemeSettings::class.java)
        }
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        val json = gson.toJson(settings)
        settingsStorage.edit {
            putString(APP_SETTINGS, json)
        }
    }

    companion object {
        val defaultTheme=ThemeSettings()
        const val APP_SETTINGS = "Playlist_Maker_settings"
    }
}