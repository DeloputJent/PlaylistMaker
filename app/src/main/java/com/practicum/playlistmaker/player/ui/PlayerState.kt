package com.practicum.playlistmaker.player.ui

sealed class PlayerState(val isPlaying: Boolean, val buttonText: String) {

    class Default : PlayerState(false, "PLAY")

    class Prepared : PlayerState(false, "PLAY")

    class Playing : PlayerState(true, "PAUSE")

    class Paused : PlayerState(false, "PLAY")
}