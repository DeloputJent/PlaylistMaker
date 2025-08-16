package com.practicum.playlistmaker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetWorkClient(baseURL:String = "https://itunes.apple.com") {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val Service = retrofit.create(iTunesSearchAPI::class.java)
}