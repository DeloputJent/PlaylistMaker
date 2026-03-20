package com.practicum.playlistmaker.medialib.di

import com.practicum.playlistmaker.medialib.ui.PlayListsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val PlaylistsModule= module {
    viewModel {
        PlayListsViewModel(get())
    }
}