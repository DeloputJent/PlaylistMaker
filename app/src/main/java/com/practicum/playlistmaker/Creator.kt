package com.practicum.playlistmaker

import android.content.Context
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetWorkClient
import com.practicum.playlistmaker.data.sharedpreferences.SearchHistory
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl

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

}