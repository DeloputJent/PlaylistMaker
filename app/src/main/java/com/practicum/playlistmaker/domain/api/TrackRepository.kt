package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.search.domain.Track

interface TrackRepository {
    fun searchTracks(expression: String): List<Track>?
}