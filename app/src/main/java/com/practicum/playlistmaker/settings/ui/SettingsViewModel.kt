package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.ThemeSettings
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor


class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    val settingsState: MutableLiveData<ThemeSettings> = MutableLiveData()

    fun observeThemeState(): MutableLiveData<ThemeSettings> = settingsState

    fun updateSettings(newSettings: ThemeSettings) {

        settingsInteractor.updateThemeSetting(newSettings)

        settingsState.value = newSettings
    }

    fun getFactory(sharingInteractor: SharingInteractor,
                  settingsInteractor: SettingsInteractor,): ViewModelProvider.Factory = viewModelFactory {
        initializer {
            SettingsViewModel(sharingInteractor, settingsInteractor)
        }
    }

}