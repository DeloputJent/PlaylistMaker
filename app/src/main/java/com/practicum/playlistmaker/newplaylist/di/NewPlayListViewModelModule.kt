package com.practicum.playlistmaker.newplaylist.di

import com.practicum.playlistmaker.db.domain.PlaylistsInteractor
import com.practicum.playlistmaker.db.domain.impl.PlaylistsInteractorImpl
import com.practicum.playlistmaker.newplaylist.ui.NewPlayListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val NewPlayListViewModelModule = module {
    viewModel {
        NewPlayListViewModel(get(),
            androidContext())
    }

    single<PlaylistsInteractor> {
        PlaylistsInteractorImpl(get())
    }
}