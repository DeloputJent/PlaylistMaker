package com.practicum.playlistmaker.domain.api

import android.content.Context


interface ThemeRepository {
    fun applyTheme(darkThemeEnabled: Boolean)
    fun saveTheme(themeEnabled: Boolean)
    fun loadTheme():Boolean
}