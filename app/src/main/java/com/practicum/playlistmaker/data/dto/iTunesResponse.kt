package com.practicum.playlistmaker.data.dto

class iTunesResponse (
    val resultCount: String,
    val results: List<TrackDto>
): Response()