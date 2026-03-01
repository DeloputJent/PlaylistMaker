package com.practicum.playlistmaker.newplaylist.di

import com.practicum.playlistmaker.newplaylist.ui.NewPlayListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val NewPlayListViewModelModule = module {
    viewModel {
        NewPlayListViewModel()
    }
}