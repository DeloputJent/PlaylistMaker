package com.practicum.playlistmaker.search.data.sharedpreferences

interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T?
}