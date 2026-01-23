package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.sharedpreferences.Resource
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val retrofitNetWorkClient: NetworkClient):
    TrackRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = retrofitNetWorkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {emit(Resource.Error(null ,resultcode= -1))}
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

    fun getReleaseYear(releaseDate:String?):String {
        return releaseDate?.substring(0, 4) ?: ""
    }


    fun formatTrackTime(millis: String?): String {
        return if (millis.isNullOrEmpty()) "00:00"
        else SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis.toLong())
    }

    fun getCoverArtwork(artUrl: String) = artUrl.replaceAfterLast('/',"512x512bb.jpg")
}