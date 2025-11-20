package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.data.sharedpreferences.Resource
import com.practicum.playlistmaker.search.domain.Track

interface SearchHistoryRepository {
    fun saveToHistory(m: Track)
    fun getHistory(): Resource<List<Track>>
}