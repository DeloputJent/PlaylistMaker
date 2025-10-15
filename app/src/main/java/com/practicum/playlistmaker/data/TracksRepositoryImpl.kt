package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.dto.iTunesResponse
import com.practicum.playlistmaker.data.network.RetrofitNetWorkClient
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val retrofitNetWorkClient: RetrofitNetWorkClient): TrackRepository {
    var lastCode: Int=0


    override fun searchTracks(expression: String):List<Track> {
        val response = retrofitNetWorkClient.doRequest(TrackSearchRequest(expression))
        lastCode=response.resultCode

        if(response.resultCode==200) {
             var searchResults: List<Track> = (response as iTunesResponse).results.map {
                Track(
                    it.trackName,
                    it.artistName,
                   // SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis.toLong()),
                   it.trackTimeMillis,
                    it.artworkUrl100,
                    it.trackId,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }

            return searchResults
            //return searchResults
        } else {return emptyList()}
    }

    fun formatTrackTime(millis: String?): String {
        return if (millis.isNullOrEmpty()) ""
        else return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis.toLong())
    }
}