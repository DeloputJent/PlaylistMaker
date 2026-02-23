package com.practicum.playlistmaker.search.di

import com.practicum.playlistmaker.search.ui.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val SearchViewModelModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }
}