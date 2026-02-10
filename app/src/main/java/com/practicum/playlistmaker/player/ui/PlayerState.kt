package com.practicum.playlistmaker.player.ui

sealed class PlayerState(val isPlaying: Boolean, val progress: String, val isFavorite: Boolean) {

    class Default : PlayerState(false, "00:00", false )

    class Prepared(isFavorite: Boolean) : PlayerState(false, "00:00", isFavorite)

    class Playing(progress: String, isFavorite: Boolean) : PlayerState(true, progress, isFavorite)

    class Paused(progress: String, isFavorite: Boolean) : PlayerState(false, progress, isFavorite)
}