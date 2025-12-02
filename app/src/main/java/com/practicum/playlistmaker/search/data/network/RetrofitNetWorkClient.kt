package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.Response
import com.practicum.playlistmaker.search.data.network.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetWorkClient(private val iTunesService:iTunesSearchAPI): NetworkClient {
    //private val baseURL:String = "https://itunes.apple.com"
    /*val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()*/

    //val iTunesService = retrofit.create(iTunesSearchAPI::class.java)

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

