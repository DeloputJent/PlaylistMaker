package com.practicum.playlistmaker.filestorage.domain

import android.net.Uri
import com.practicum.playlistmaker.filestorage.domain.api.FileStorageInteractor
import com.practicum.playlistmaker.filestorage.domain.api.FileStorageRepository

class FileStorageInteractorImpl(private val repository: FileStorageRepository):
    FileStorageInteractor {
    override fun getFile(pathToArtwork: String): Uri {
        return repository.getFile(pathToArtwork)
    }

    override fun saveToStorage(uri: Uri, playlistName: String): String {
       return repository.saveToStorage(uri, playlistName)
    }
}