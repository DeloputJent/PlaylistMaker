package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.practicum.playlistmaker.data.ThemeRepositoryImpl

import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.musicplayer.MusicPlayerRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetWorkClient
import com.practicum.playlistmaker.data.sharedpreferences.SearchHistory
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.ui.IntentProvider

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

    fun getMediaPlayer(): MusicPlayerRepositoryImpl {
        val mediaPlayer = MediaPlayer()
        return MusicPlayerRepositoryImpl(mediaPlayer)
    }

    fun getIntentProvider(context: Context): IntentProvider {
        return IntentProvider(context)
    }

    fun getThemeRepositoryImpl(context: Context): ThemeRepositoryImpl {
        return ThemeRepositoryImpl(context)
    }
}