package com.practicum.playlistmaker.creator

import android.content.Context
import com.google.gson.reflect.TypeToken

import com.practicum.playlistmaker.settings.data.ThemeInteractorImpl
import com.practicum.playlistmaker.settings.data.ThemeRepositoryImpl
import com.practicum.playlistmaker.search.data.network.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetWorkClient
import com.practicum.playlistmaker.search.data.storage.PrefsStorageClient
import com.practicum.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.settings.domain.api.ThemeInteractor
import com.practicum.playlistmaker.settings.domain.api.ThemeRepository
import com.practicum.playlistmaker.search.domain.TrackRepository
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.sharing.data.ExternalNavigator

object Creator {

    private fun getTracksRepository(): TrackRepository {
        return TracksRepositoryImpl(RetrofitNetWorkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun getIntentProvider(context: Context): ExternalNavigator {
        return ExternalNavigator(context)
    }

    fun getThemeRepository(context: Context): ThemeRepository {
        return ThemeRepositoryImpl(context)
    }

    fun getThemeInteractor(): ThemeInteractor {
        return ThemeInteractorImpl()
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            PrefsStorageClient<ArrayList<Track>>(
                context,
                "HISTORY",
                object : TypeToken<ArrayList<Track>>() {}.type
            )
        )
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }
}