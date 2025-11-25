package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.network.Response
import com.practicum.playlistmaker.search.data.TrackDto

class iTunesResponse (
    val resultCount: String,
    val results: List<TrackDto>
): Response()