package com.practicum.playlistmaker.playlist.di

import com.practicum.playlistmaker.playlist.ui.PlayListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val CurrentPlaylistModule = module {
    viewModel {
        PlayListViewModel(get(), get())
    }
}