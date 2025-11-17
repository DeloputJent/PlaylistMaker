package com.practicum.playlistmaker.search.domain

sealed interface SearchTrackState {
    data class Content (val tracks: List<Track>) : SearchTrackState
    data class ShowHistory (val tracks: List<Track>) : SearchTrackState
    object Loading : SearchTrackState
    object NothingFound : SearchTrackState
    object NoNetFound : SearchTrackState
}