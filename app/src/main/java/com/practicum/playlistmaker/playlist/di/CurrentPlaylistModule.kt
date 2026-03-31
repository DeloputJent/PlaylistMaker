package com.practicum.playlistmaker.playlist.di

import com.practicum.playlistmaker.filestorage.data.FileStorageRepositoryImpl
import com.practicum.playlistmaker.playlist.data.PlayListExternalNavigator
import com.practicum.playlistmaker.filestorage.domain.FileStorageInteractorImpl
import com.practicum.playlistmaker.playlist.domain.PlaylistSharingInteractorImpl
import com.practicum.playlistmaker.filestorage.domain.api.FileStorageInteractor
import com.practicum.playlistmaker.filestorage.domain.api.FileStorageRepository
import com.practicum.playlistmaker.playlist.domain.api.PlaylistIntentProvider
import com.practicum.playlistmaker.playlist.domain.api.PlaylistSharingInteractor
import com.practicum.playlistmaker.playlist.ui.PlayListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val CurrentPlaylistModule = module {
    viewModel {
        PlayListViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }

    single<PlaylistIntentProvider> {
        PlayListExternalNavigator(androidContext())
    }

    single<FileStorageRepository> {
        FileStorageRepositoryImpl(androidContext())
    }

    factory<FileStorageInteractor> {
        FileStorageInteractorImpl(get())
    }

    factory< PlaylistSharingInteractor> {
        PlaylistSharingInteractorImpl(get())
    }
}