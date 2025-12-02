package com.practicum.playlistmaker.sharing.di

import android.content.Intent
import android.net.Uri
import org.koin.dsl.module

val dataModule = module {
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