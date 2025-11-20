package com.practicum.playlistmaker.search.domain

interface TracksInteractor {
    fun searchTracks(expression: String, consumer:TracksConsumer)
    interface TracksConsumer{
        fun consume(foundTracks: List<Track>?)
    }
}