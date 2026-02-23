package com.practicum.playlistmaker.settings.di

import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val SettingsViewModelModule = module {
    viewModel {
        SettingsViewModel(get(), get())
    }
}