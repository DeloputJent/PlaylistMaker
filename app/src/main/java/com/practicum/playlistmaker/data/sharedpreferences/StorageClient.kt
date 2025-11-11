package com.practicum.playlistmaker.data.sharedpreferences

interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T?
}