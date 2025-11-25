package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.sharedpreferences.Resource
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val storage: StorageClient<ArrayList<Track>>
): SearchHistoryRepository {

    override fun saveToHistory(t: Track) {
        val tracks = storage.getData() ?: arrayListOf()
        tracks.removeIf { it.trackId == t.trackId }
        if (tracks.size == 10) tracks.removeAt(9)
        tracks.add(0, t)
        storage.storeData(tracks)
    }

    override fun getHistory(): Resource<List<Track>> {
        val tracks = storage.getData() ?: listOf()
        return Resource.Success(tracks)
    }

    override fun clearHistory() {
        storage.clearData()
    }
}