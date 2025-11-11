package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.dto.iTunesResponse
import com.practicum.playlistmaker.data.network.RetrofitNetWorkClient
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val retrofitNetWorkClient: RetrofitNetWorkClient): TrackRepository {
    var lastCode: Int=0

    override fun searchTracks(expression: String): List<Track>? {
        val response = retrofitNetWorkClient.doRequest(TrackSearchRequest(expression))
        if(response.resultCode==-1) {
            return null
        } else {
            if(response.resultCode==200) {
                return (response as iTunesResponse).results.map {
                    Track(
                        it.trackName,
                        it.artistName,
                        formatTrackTime(it.trackTimeMillis),
                        it.artworkUrl100,
                        it.trackId,
                        it.collectionName,
                        getReleaseYear(it.releaseDate),
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl,
                        getCoverArtwork(it.artworkUrl100)
                    )
                }
            } else {return emptyList()}
        }
    }

    fun getReleaseYear(releaseDate:String?):String {
        return releaseDate?.substring(0, 4) ?: ""
    }


    fun formatTrackTime(millis: String?): String {
        return if (millis.isNullOrEmpty()) "00:00"
        else SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis.toLong())
    }

    fun getCoverArtwork(artUrl: String) = artUrl.replaceAfterLast('/',"512x512bb.jpg")
}