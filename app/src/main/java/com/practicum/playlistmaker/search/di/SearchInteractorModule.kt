package com.practicum.playlistmaker.search.di

import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import org.koin.dsl.module


val SearchInteractorModule = module{

    factory<TracksInteractor>{
        TracksInteractorImpl(get())
    }

    factory<SearchHistoryInteractor>{
        SearchHistoryInteractorImpl(get())
    }
}