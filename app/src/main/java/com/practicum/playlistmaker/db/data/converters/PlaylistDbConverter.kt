package com.practicum.playlistmaker.db.data.converters

import com.practicum.playlistmaker.db.data.entity.PlayListEntity
import com.practicum.playlistmaker.db.data.entity.TracksInPlaylistsEntity
import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.search.domain.Track

class PlaylistDbConverter {
    fun map(playList: PlayListEntity): Playlist {
        return Playlist(
            playList.playlistID,
            playList.playlistName,
            playList.playlistDescription,
            playList.pathToArtwork,
            playList.tracksId,
            playList.tracksAmount
        )
    }

    fun map(playList: Playlist): PlayListEntity {
        return PlayListEntity(
            playList.playlistID,
            playList.playlistName,
            playList.playlistDescription,
            playList.pathToArtwork,
            playList.tracksId,
            playList.tracksAmount
        )
    }

    fun mapTrack(track: TracksInPlaylistsEntity): Track {
        return Track(track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100,
            track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country,
            track.previewUrl, track.coverArtworkUrl,track.isFavorite)
    }

    fun mapTrack(track: Track): TracksInPlaylistsEntity {
        return TracksInPlaylistsEntity(track.trackId, track.trackName, track.artistName, track.trackTimeMillis,
            track.artworkUrl100, track.collectionName, track.releaseDate, track.primaryGenreName,
            track.country, track.previewUrl, track.coverArtworkUrl,track.isFavorite)
    }
}