package com.practicum.playlistmaker.search.di

import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.StorageClient
import com.practicum.playlistmaker.search.data.network.RetrofitNetWorkClient
import com.practicum.playlistmaker.search.data.network.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.storage.PrefsStorageClient
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TrackRepository
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val SearchRepositoryModule = module {
    single<TrackRepository> {
        TracksRepositoryImpl(RetrofitNetWorkClient(get()))
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

    factory<StorageClient<ArrayList<Track>>> {
        PrefsStorageClient(
            androidContext(),
            get(),
            object : TypeToken<ArrayList<Track>>() {}.type
        )
    }
}