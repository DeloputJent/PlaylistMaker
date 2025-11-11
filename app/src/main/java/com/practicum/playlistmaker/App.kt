package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.data.ThemeInteractorImpl
import com.practicum.playlistmaker.data.ThemeRepositoryImpl

class App : Application() {

    lateinit var themeRepository: ThemeRepositoryImpl

    lateinit var themeInteractorImpl: ThemeInteractorImpl

    fun applyTheme (themeEnabled: Boolean) {
        themeInteractorImpl.applyTheme(themeEnabled)
        themeRepository.saveTheme(themeEnabled)
    }

    fun loadTheme(): Boolean = themeRepository.loadTheme()

    override fun onCreate() {
        super.onCreate()
        themeRepository = Creator.getThemeRepository(this) as ThemeRepositoryImpl
        themeInteractorImpl = Creator.getThemeInteractor() as ThemeInteractorImpl
        applyTheme(loadTheme())
    }
}