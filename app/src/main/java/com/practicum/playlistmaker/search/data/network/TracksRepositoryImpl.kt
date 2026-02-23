package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.util.Resource
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
            -1 -> {emit(Resource.Error(null))}
            200 -> {
                with (response as ITunesResponse) {
                    val data = response.results.map {
                        Track(
                            it.trackName,
                            it.artistName,
                            it.formatTrackTime(),
                            it.artworkUrl100,
                            it.trackId,
                            it.collectionName,
                            it.getReleaseYear(),
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl,
                            it.getCoverArtwork()
                        )
                    }
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error(emptyList()))
            }
        }
    }
}