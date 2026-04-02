package com.practicum.playlistmaker.newplaylist.ui

import android.content.Context
import android.net.Uri
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

    override var playListName: String = ""
    override var playListDescription: String = ""

    override var uri: Uri = Uri.EMPTY
    val currentPlaylistLiveData = MutableLiveData<Playlist>(currentPlaylist)

    fun observeCurrentPlaylist(): LiveData<Playlist> = currentPlaylistLiveData

    override fun createPlayList(playlistName: String,
                                playlistDescription:String,
                                pathToArtwork:String) {
        viewModelScope.launch {
            val path = if (pathToArtwork=="") currentPlaylist.pathToArtwork
            else pathToArtwork
            val playlist = Playlist(
                playlistID = currentPlaylist.playlistID,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                pathToArtwork = path,
                tracksId = currentPlaylist.tracksId,
                tracksAmount = currentPlaylist.tracksAmount
            )
            //currentPlaylistLiveData.postValue(playlist)
            playlistsInteractor.updatePlaylist(playlist)
        }
    }

    fun getPlaylistById(playlistId: Int) {
            viewModelScope.launch {
                currentPlaylist = playlistsInteractor.getPlaylistById(playlistId)
                currentPlaylistLiveData.postValue(currentPlaylist)
            }
    }

    override fun saveImageToPrivateStorage(uri: Uri, playlistName: String): String {
        val picName=playlistName + "_Artwork" + ".jpg"
        Log.d("FileStorage", "uri=${uri.toString()}")
        val isDeleted = fileInteractor.deleteFile(picName)
        Log.d("FileStorage", "isDeleted=${isDeleted.toString()}")
        return fileInteractor.saveToStorage(uri, picName)
    }
}