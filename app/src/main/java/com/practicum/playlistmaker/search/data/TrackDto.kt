package com.practicum.playlistmaker.search.data

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

class TrackDto (val trackName: String,
                val artistName: String,
                val trackTimeMillis: String,
                val artworkUrl100: String,
                val trackId: String,
                val collectionName: String,
                val releaseDate: String,
                val primaryGenreName: String,
                val country: String,
                val previewUrl: String,
): Serializable {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    fun formatTrackTime():String = SimpleDateFormat("mm:ss", Locale.getDefault())
        .format(trackTimeMillis.toLong())
    fun getReleaseYear():String {
        return releaseDate.substring(0, 4)
    }
}