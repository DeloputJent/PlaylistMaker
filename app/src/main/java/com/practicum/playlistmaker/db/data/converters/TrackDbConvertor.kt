package com.practicum.playlistmaker.db.data.converters

import com.practicum.playlistmaker.db.data.entity.TrackEntity
import com.practicum.playlistmaker.search.domain.Track

class TrackDbConvertor {
    fun map(track: TrackEntity): Track {
        return Track(track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100,
            track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country,
            track.previewUrl, track.coverArtworkUrl,track.isFavorite)
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(track.trackId, track.trackName, track.artistName, track.trackTimeMillis,
            track.artworkUrl100, track.collectionName, track.releaseDate, track.primaryGenreName,
            track.country, track.previewUrl, track.coverArtworkUrl,track.isFavorite)
    }
}