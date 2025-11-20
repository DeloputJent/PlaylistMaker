package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.Track

interface SearchHistoryInteractor {

    fun getHistory(consumer: HistoryConsumer)
    fun saveToHistory(m: Track)

    interface HistoryConsumer {
        fun consume(searchHistory: List<Track>?)
    }
}