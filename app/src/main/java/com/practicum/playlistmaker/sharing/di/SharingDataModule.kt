package com.practicum.playlistmaker.sharing.di

import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.IntentProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val SharingDataModule = module {
    single<IntentProvider> {
        ExternalNavigator(androidContext())
    }
}