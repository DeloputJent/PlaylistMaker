package com.practicum.playlistmaker.search.data.util

sealed class Resource<T>(val data: T? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(data: T? = null): Resource<T>(data)
    object Loading : Resource<Nothing>()
}