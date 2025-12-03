package com.practicum.playlistmaker.search.di

import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.RetrofitNetWorkClient
import com.practicum.playlistmaker.search.data.network.iTunesSearchAPI
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val SearchDataModule = module {

    single<iTunesSearchAPI>{
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(iTunesSearchAPI::class.java)
    }

    single<NetworkClient>{
        RetrofitNetWorkClient(get())
    }

    factory { Gson() }
}