package com.practicum.playlistmaker.newplaylist.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.PlaylistsInteractor
import com.practicum.playlistmaker.filestorage.domain.api.FileStorageInteractor
import com.practicum.playlistmaker.medialib.domain.Playlist
import kotlinx.coroutines.launch

open class NewPlayListViewModel(
    val playlistsInteractor: PlaylistsInteractor,
    val fileInteractor: FileStorageInteractor,
    val context: Context,
) : ViewModel() {
    open var playListName: String = ""
    open var playListDescription: String = ""

    open var uri: Uri = Uri.EMPTY

    fun isDescriptionEmpty(): Boolean {
    return playListName.isEmpty() and playListDescription.isEmpty()
    }

    fun getImageUri(pathToArtwork:String): Uri {
        return fileInteractor.getFile(pathToArtwork)
    }

    open fun createPlayList(playlistName: String, playlistDescription:String, pathToArtwork:String) {
        viewModelScope.launch {
            val playlist = Playlist(
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                pathToArtwork = pathToArtwork,
            )
            playlistsInteractor.addNewPlaylist(playlist)
        }
    }

    open fun saveImageToPrivateStorage(uri: Uri, playlistName: String): String {
        val picName=playlistName + "_Artwork" + ".jpg"
        return fileInteractor.saveToStorage(uri, picName)
    }
}