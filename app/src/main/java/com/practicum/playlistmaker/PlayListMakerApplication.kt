package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.player.di.PlayerViewModelModule
import com.practicum.playlistmaker.search.di.SearchDataModule
import com.practicum.playlistmaker.search.di.SearchInteractorModule
import com.practicum.playlistmaker.search.di.SearchRepositoryModule
import com.practicum.playlistmaker.search.di.SearchViewModelModule
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.di.SettingsInteractorModule
import com.practicum.playlistmaker.settings.di.SettingsRepositoryModule
import com.practicum.playlistmaker.settings.di.SettingsViewModelModule
import com.practicum.playlistmaker.sharing.di.SharingDataModule
import com.practicum.playlistmaker.sharing.di.SharingInteractorModule
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PlayListMakerApplication():Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PlayListMakerApplication)
            modules(PlayerViewModelModule,
                SearchDataModule,
                SearchInteractorModule,
                SearchRepositoryModule,
                SearchViewModelModule,
                SharingDataModule,
                SharingInteractorModule,
                SettingsInteractorModule,
                SettingsRepositoryModule,
                SettingsViewModelModule,
            )
        }

        val settingsRepository = get<SettingsRepository>()

        val themeSettings = settingsRepository.getThemeSettings()

        AppCompatDelegate.setDefaultNightMode(
            if (themeSettings.darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}