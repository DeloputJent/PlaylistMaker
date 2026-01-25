package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.data.util.Resource
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TrackRepository
import com.practicum.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TrackRepository): TracksInteractor {

    override fun searchTracks(expression: String): Flow<List<Track>?> {
        return repository.searchTracks(expression).map { result ->
            when(result) {
                is Resource.Success -> {result.data}
                else -> null
            }
        }
    }
}