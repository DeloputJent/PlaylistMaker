package com.practicum.playlistmaker.playlist.ui

import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.search.domain.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlayListScreen (val playList: Playlist, val tracks: List<Track>) {
    var summaryTime: Int = 0

    var tracksAmount: Int = 0

    init {
        var timeOfAllTracks: Long = 0
        tracks.forEach {
                track -> timeOfAllTracks += convertTimeToMillis(track.trackTimeMillis)
        }
        summaryTime = SimpleDateFormat("mm", Locale.getDefault()).format(timeOfAllTracks).toInt()
        tracksAmount = tracks.size
    }

    private fun convertTimeToMillis(timeString: String): Long {
        val format = SimpleDateFormat("mm:ss", Locale.getDefault())
        val date: Date? = format.parse(timeString)
        return date?.time ?: 0L
    }
}