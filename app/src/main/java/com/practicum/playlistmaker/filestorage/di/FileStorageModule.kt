package com.practicum.playlistmaker.filestorage.di

import com.practicum.playlistmaker.filestorage.data.FileStorageRepositoryImpl
import com.practicum.playlistmaker.filestorage.domain.FileStorageInteractorImpl
import com.practicum.playlistmaker.filestorage.domain.api.FileStorageInteractor
import com.practicum.playlistmaker.filestorage.domain.api.FileStorageRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val FileStorageModule = module {

    single<FileStorageRepository> {
        FileStorageRepositoryImpl(androidContext())
    }

    single<FileStorageInteractor> {
        FileStorageInteractorImpl(get())
    }
}