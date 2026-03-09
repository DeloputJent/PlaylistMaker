package com.practicum.playlistmaker.medialib.domain

data class Playlist(
    val playlistID:Int=0,
    val playlistName:String,
    val playlistDescription:String="",
    val pathToArtwork:String="",
    val tracksId:String="",
    val tracksAmount:Int=0,
)