package com.practicum.playlistmaker.playlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.db.domain.PlaylistsInteractor
import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.launch

class PlayListViewModel(private val dbinteractor: PlaylistsInteractor, private val gson : Gson): ViewModel() {

    var currentPlaylist: Playlist = Playlist()

    private val currentPlaylistLiveData = MutableLiveData <PlayListScreen> ( )

    fun observeCurrentPlaylist(): LiveData<PlayListScreen> = currentPlaylistLiveData

    fun getPlaylistsById(playlistId:Int) {
        viewModelScope.launch {
            currentPlaylist = dbinteractor.getPlaylistById(playlistId)
            dbinteractor.getTracksFromPlaylist(getTracksID(currentPlaylist))
                .collect {
                    playListTracks -> currentPlaylistLiveData.postValue(
                    PlayListScreen(currentPlaylist, playListTracks)
                    )
                }
        }
    }
    fun getTracksID(playlist: Playlist): List<String> {
        return if (!playlist.tracksId.isNullOrEmpty())
            gson.fromJson<MutableList<String>>(
                playlist.tracksId,
                object : TypeToken<List<String>>(){}.type)
        else emptyList()
    }
    fun deleteTrackFromPlaylist (track: Track, playlistId:Int) {
        viewModelScope.launch {
            dbinteractor.deleteTrackFromPlaylist(track.trackId, currentPlaylist.playlistID)
            currentPlaylist = dbinteractor.getPlaylistById(playlistId)
            dbinteractor.getTracksFromPlaylist(getTracksID(currentPlaylist))
                .collect {
                        playListTracks -> currentPlaylistLiveData.postValue(
                    PlayListScreen(currentPlaylist, playListTracks)
                )
                }
        }
    }
}