package com.practicum.playlistmaker.medialib.ui.presentation

import com.practicum.playlistmaker.search.domain.Track

sealed interface FavoriteTrackState {
    data class Content (val tracks: List<Track>) : FavoriteTrackState
    object Loading : FavoriteTrackState
    object NothingFound : FavoriteTrackState
}