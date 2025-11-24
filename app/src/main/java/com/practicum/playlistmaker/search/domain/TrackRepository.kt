package com.practicum.playlistmaker.search.domain

interface TrackRepository {
    fun searchTracks(expression: String): List<Track>?
}