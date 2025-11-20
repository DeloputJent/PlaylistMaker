package com.practicum.playlistmaker.settings.domain.api


interface ThemeRepository {
    fun saveTheme(themeEnabled: Boolean)
    fun loadTheme():Boolean
}