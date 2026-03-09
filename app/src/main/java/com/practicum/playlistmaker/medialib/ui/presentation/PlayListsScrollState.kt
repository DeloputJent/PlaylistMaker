package com.practicum.playlistmaker.medialib.ui.presentation

import com.practicum.playlistmaker.medialib.domain.Playlist

interface PlayListsScrollState {
    data class Content (val playLists: List<Playlist>) : PlayListsScrollState
    object Loading : PlayListsScrollState
    object NoPlaylistsFound : PlayListsScrollState
}