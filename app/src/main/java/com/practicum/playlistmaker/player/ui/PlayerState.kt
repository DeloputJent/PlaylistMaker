package com.practicum.playlistmaker.player.ui

sealed class PlayerState(val isPlaying: Boolean, val progress: String,) {

    class Default() : PlayerState(false, "00:00",)

    class Prepared() : PlayerState(false, "00:00",)

    class Playing(progress: String) : PlayerState(true, progress,)

    class Paused(progress: String) : PlayerState(false, progress,)
}