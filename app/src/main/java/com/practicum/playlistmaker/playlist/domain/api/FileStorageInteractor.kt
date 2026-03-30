package com.practicum.playlistmaker.playlist.domain.api

import android.net.Uri

interface FileStorageInteractor {
    fun getFile(pathToArtwork: String): Uri
}