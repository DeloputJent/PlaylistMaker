package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.dto.iTunesResponse
import com.practicum.playlistmaker.data.network.RetrofitNetWorkClient
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val retrofitNetWorkClient: RetrofitNetWorkClient): TrackRepository {
    var lastCode: Int=0

    override fun searchTracks(expression: String):MutableList<Track> {
        val response = retrofitNetWorkClient.doRequest(TrackSearchRequest(expression))
        lastCode=response.resultCode
        if(response.resultCode==200) {
            return ((response as iTunesResponse).results.map {
                Track(
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.trackId,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }).toMutableList()
        } else {return mutableListOf()}
    }


}