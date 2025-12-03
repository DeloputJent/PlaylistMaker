package com.practicum.playlistmaker.sharing.di

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.IntentProvider
import org.koin.dsl.module

val SharingDataModule = module {

    single<IntentProvider> { ExternalNavigator(get(), get(), get(), get()) }

    single<Intent> {
        Intent(Intent.ACTION_SEND)
    }

    single<Intent> {
        Intent(Intent.ACTION_SENDTO)
    }

    single<Intent> {
        (url: Uri)->Intent(Intent.ACTION_VIEW, url)
    }
}