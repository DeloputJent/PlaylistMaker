package com.practicum.playlistmaker.playlist.domain.api

interface PlaylistIntentProvider {
    fun shareText(message: List<String>)
}