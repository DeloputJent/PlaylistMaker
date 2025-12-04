package com.practicum.playlistmaker.search.data.network

class RetrofitNetWorkClient(private val iTunesService:iTunesSearchAPI): NetworkClient {
    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            return try {
                val resp = iTunesService.search(dto.expression).execute()
                val body = resp.body() ?: Response()
                body.apply { resultCode = resp.code() }
            } catch (e: Exception) {
                Response().apply { resultCode = -1 }
            }
        } else {
            return Response().apply { resultCode=400 }
        }
    }
}

