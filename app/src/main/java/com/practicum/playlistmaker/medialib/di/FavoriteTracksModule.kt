package com.practicum.playlistmaker.medialib.di

import com.practicum.playlistmaker.medialib.ui.presentation.FavoriteViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val FavoriteTracksModule = module {
    viewModel {
        FavoriteViewModel(get())
    }
}