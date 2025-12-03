package com.practicum.playlistmaker.search.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetWorkClient
import com.practicum.playlistmaker.search.data.network.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.storage.PrefsStorageClient
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TrackRepository
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import org.koin.dsl.module

val SearchRepositoryModule = module {
    single<TrackRepository> {
        TracksRepositoryImpl(RetrofitNetWorkClient(get()))
    }

    factory<SearchHistoryRepository> {
            (context: Context) -> SearchHistoryRepositoryImpl(
        PrefsStorageClient(context, Gson(),
        object : TypeToken<ArrayList<Track>>() {}.type))
    }
}