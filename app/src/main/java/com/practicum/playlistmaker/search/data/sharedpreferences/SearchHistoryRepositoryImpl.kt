package com.practicum.playlistmaker.search.data.sharedpreferences

import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<ArrayList<Track>>): SearchHistoryRepository {

    override fun saveToHistory(m: Track) {
        val tracks = storage.getData() ?: arrayListOf()
        tracks.add(m)
        storage.storeData(tracks)
    }

    override fun getHistory(): Resource<List<Track>> {
        val tracks = storage.getData() ?: listOf()
        return Resource.Success(tracks)
    }
}