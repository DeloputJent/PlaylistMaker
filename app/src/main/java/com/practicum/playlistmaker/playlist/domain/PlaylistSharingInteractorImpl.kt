package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.playlist.domain.api.PlaylistIntentProvider
import com.practicum.playlistmaker.playlist.domain.api.PlaylistSharingInteractor
import kotlin.String

class PlaylistSharingInteractorImpl(
    private val externalNavigator: PlaylistIntentProvider,
) : PlaylistSharingInteractor {

    override fun sharePlaylist(message: List<String>) {
        externalNavigator.shareText(message)
    }

}