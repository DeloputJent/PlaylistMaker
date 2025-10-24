package com.practicum.playlistmaker.domain.api


interface ThemeRepository {
    fun saveTheme(themeEnabled: Boolean)
    fun loadTheme():Boolean
}