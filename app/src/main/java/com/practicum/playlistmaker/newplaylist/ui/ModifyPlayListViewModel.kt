package com.practicum.playlistmaker.newplaylist.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.PlaylistsInteractor
import com.practicum.playlistmaker.medialib.domain.Playlist
import kotlinx.coroutines.launch

class ModifyPlayListViewModel(playlistsInteractor: PlaylistsInteractor, context: Context): NewPlayListViewModel(
    playlistsInteractor, context
) {
    var currentPlaylist: Playlist = Playlist()

    override fun createPlayList(playlistName: String, playlistDescription:String, pathToArtwork:String) {
        val path = if (pathToArtwork=="") currentPlaylist.pathToArtwork
        else pathToArtwork

        viewModelScope.launch {
            val playlist = Playlist(
                playlistID = currentPlaylist.playlistID,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                pathToArtwork = path,
                tracksId = currentPlaylist.tracksId,
                tracksAmount = currentPlaylist.tracksAmount
            )
            playlistsInteractor.updatePlaylist(playlist)
        }
    }

    private val currentPlaylistLiveData = MutableLiveData<Playlist>()

    fun observeCurrentPlaylist(): LiveData<Playlist> = currentPlaylistLiveData

    fun getPlaylistById(playlistId: Int) {
            viewModelScope.launch {
                currentPlaylist = playlistsInteractor.getPlaylistById(playlistId)
                currentPlaylistLiveData.postValue(currentPlaylist)
            }
    }
}