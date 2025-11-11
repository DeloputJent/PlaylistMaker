package com.practicum.playlistmaker.creator

import android.content.Context

import com.practicum.playlistmaker.data.ThemeInteractorImpl
import com.practicum.playlistmaker.data.ThemeRepositoryImpl
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetWorkClient
import com.practicum.playlistmaker.data.sharedpreferences.SearchHistory
import com.practicum.playlistmaker.domain.api.ThemeInteractor
import com.practicum.playlistmaker.domain.api.ThemeRepository
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.settings.data.ExternalNavigator

object Creator {

    private fun getTracksRepository(): TrackRepository {
        return TracksRepositoryImpl(RetrofitNetWorkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

   fun getHistoryOfSearch(context: Context): SearchHistory {
        return SearchHistory(context)
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
}