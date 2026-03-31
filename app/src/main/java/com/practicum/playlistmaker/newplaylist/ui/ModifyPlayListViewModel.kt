package com.practicum.playlistmaker.newplaylist.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.PlaylistsInteractor
import com.practicum.playlistmaker.filestorage.domain.api.FileStorageInteractor
import com.practicum.playlistmaker.medialib.domain.Playlist
import kotlinx.coroutines.launch

class ModifyPlayListViewModel(playlistsInteractor: PlaylistsInteractor,
                              fileInteractor: FileStorageInteractor,
                              context: Context
): NewPlayListViewModel(playlistsInteractor, fileInteractor, context) {
    var currentPlaylist: Playlist = Playlist()

    override fun createPlayList(playlistName: String, playlistDescription:String, pathToArtwork:String) {
            val path = if (pathToArtwork=="") currentPlaylist.pathToArtwork
            else pathToArtwork
            Log.d("Image", "playlistName="+path)
            val playlist = Playlist(
                playlistID = currentPlaylist.playlistID,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                pathToArtwork = path,
                tracksId = currentPlaylist.tracksId,
                tracksAmount = currentPlaylist.tracksAmount
            )
        currentPlaylistLiveData.postValue(playlist)
        viewModelScope.launch {
            playlistsInteractor.updatePlaylist(playlist)
        }
    }

    val currentPlaylistLiveData = MutableLiveData<Playlist>()

    fun observeCurrentPlaylist(): LiveData<Playlist> = currentPlaylistLiveData

    fun getPlaylistById(playlistId: Int) {
            viewModelScope.launch {
                currentPlaylist = playlistsInteractor.getPlaylistById(playlistId)
                currentPlaylistLiveData.postValue(currentPlaylist)
            }
    }
}