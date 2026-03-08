package com.practicum.playlistmaker.db.data.converters

import com.practicum.playlistmaker.db.data.entity.PlayListEntity
import com.practicum.playlistmaker.newplaylist.domain.Playlist

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
        return PlayListEntity(playList.playlistID,
            playList.playlistName,
            playList.playlistDescription,
            playList.pathToArtwork,
            playList.tracksId,
            playList.tracksAmount
        )
    }
}