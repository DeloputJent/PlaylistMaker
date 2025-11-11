package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.search.domain.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer:TracksConsumer)
    interface TracksConsumer{
        fun consume(foundTracks: List<Track>?)
    }
}