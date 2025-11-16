package com.practicum.playlistmaker.search.domain

sealed interface SearchTrackState {
    object Loading : SearchTrackState
    data class Content (val tracks: List<Track>) : SearchTrackState
    object NothingFound : SearchTrackState
    object NoNetFound : SearchTrackState
}