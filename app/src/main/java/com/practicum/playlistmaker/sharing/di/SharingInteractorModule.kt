package com.practicum.playlistmaker.sharing.di

import com.practicum.playlistmaker.sharing.domain.SharingInteractorImpl
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import org.koin.dsl.module

val SharingInteractorModule = module {

    single { SharingInteractorImpl(get())}

}