package com.practicum.playlistmaker.settings.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val SettingsRepositoryModule = module {
    factory<SettingsRepository> { SettingsRepositoryImpl(get(), get()) }
    single<SharedPreferences> { androidContext().getSharedPreferences(SettingsRepositoryImpl.APP_SETTINGS,Context.MODE_PRIVATE) }
    factory { Gson() }
}