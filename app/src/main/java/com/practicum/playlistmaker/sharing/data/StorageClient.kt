package com.practicum.playlistmaker.sharing.data

interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T?
}