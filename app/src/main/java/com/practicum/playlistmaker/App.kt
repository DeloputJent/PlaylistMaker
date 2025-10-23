package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.data.ThemeRepositoryImpl

class App : Application() {

    lateinit var themeRepository: ThemeRepositoryImpl

    fun applyTheme (themeEnabled: Boolean) {
        themeRepository.applyTheme(themeEnabled)
    }

    fun loadTheme(): Boolean = themeRepository.loadTheme()

    override fun onCreate() {
        super.onCreate()
        themeRepository = Creator.getThemeRepositoryImpl(this)
    }
}