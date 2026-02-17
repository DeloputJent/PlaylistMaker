package com.practicum.playlistmaker.player.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.db.data.impl.FavoriteRepositoryImpl
import com.practicum.playlistmaker.db.domain.FavoriteInteractor
import com.practicum.playlistmaker.db.domain.FavoriteRepository
import com.practicum.playlistmaker.db.domain.impl.FavoriteInteractorImpl
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val PlayerViewModelModule = module {
    viewModel { (track: Track)->
        PlayerViewModel(track, get(), get())
    }

    single<FavoriteInteractor>{
        FavoriteInteractorImpl(get())
    }

    factory { MediaPlayer() }
}