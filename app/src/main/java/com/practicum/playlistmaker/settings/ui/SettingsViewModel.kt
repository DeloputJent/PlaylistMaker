package com.practicum.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.settings.domain.ThemeSettings
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import org.koin.core.component.KoinComponent

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel(), KoinComponent {
    val settingsState: MutableLiveData<ThemeSettings> = MutableLiveData(defaultSettings)

    fun observeThemeState(): LiveData<ThemeSettings> {
        settingsState.value = settingsInteractor.getThemeSettings()
        return settingsState
    }
    fun updateSettings(newSettings: ThemeSettings) {
        settingsInteractor.updateThemeSetting(newSettings)
        settingsState.value = newSettings
        settingsState.postValue(newSettings)
        switchNightMode(newSettings.darkThemeEnabled)
    }

    fun switchNightMode (darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    companion object {

        val defaultSettings = ThemeSettings(false)

        fun getFactory(
            sharingInteractor: SharingInteractor,
            settingsInteractor: SettingsInteractor,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(sharingInteractor, settingsInteractor)
            }
        }
    }
}