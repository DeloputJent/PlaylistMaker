package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.TrackDto

class ITunesResponse (
    val searchType: String,
    val expression: String,
    val results: List<TrackDto>
): Response()