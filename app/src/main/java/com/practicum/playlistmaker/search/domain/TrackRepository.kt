package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.data.sharedpreferences.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}