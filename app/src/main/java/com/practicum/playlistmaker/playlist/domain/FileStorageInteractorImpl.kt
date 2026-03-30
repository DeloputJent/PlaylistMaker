package com.practicum.playlistmaker.playlist.domain

import android.net.Uri
import com.practicum.playlistmaker.playlist.domain.api.FileStorageInteractor
import com.practicum.playlistmaker.playlist.domain.api.FileStorageRepository

class FileStorageInteractorImpl(private val repository: FileStorageRepository): FileStorageInteractor {
    override fun getFile(pathToArtwork: String): Uri {
        return repository.getFile(pathToArtwork)
    }
}