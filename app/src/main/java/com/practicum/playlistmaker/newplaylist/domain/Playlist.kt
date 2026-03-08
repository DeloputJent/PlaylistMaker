package com.practicum.playlistmaker.newplaylist.domain

data class Playlist(
    val playlistID:Int,
    val playlistName:String,
    val playlistDescription:String,
    val pathToArtwork:String,
    val tracksId:String,
    val tracksAmount:Int,
)
