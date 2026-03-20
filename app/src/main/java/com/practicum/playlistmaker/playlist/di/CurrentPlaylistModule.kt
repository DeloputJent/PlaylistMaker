package com.practicum.playlistmaker.playlist.di

import com.practicum.playlistmaker.medialib.ui.PlayListsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val CurrentPlaylistModule = module {
    viewModel {
        PlayListsViewModel(get())
    }
}