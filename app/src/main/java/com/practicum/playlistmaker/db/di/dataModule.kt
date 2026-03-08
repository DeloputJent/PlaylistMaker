package com.practicum.playlistmaker.db.di

import androidx.room.Room
import com.practicum.playlistmaker.db.PlaylistsDatabase
import com.practicum.playlistmaker.db.TrackDatabase
import com.practicum.playlistmaker.db.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.db.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.db.data.impl.FavoriteRepositoryImpl
import com.practicum.playlistmaker.db.data.impl.PlaylistsRepositoryImpl
import com.practicum.playlistmaker.db.domain.FavoriteRepository
import com.practicum.playlistmaker.db.domain.PlaylistsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), TrackDatabase::class.java, "database.db")
            .build()
    }

    single{
        Room.databaseBuilder(androidContext(), PlaylistsDatabase::class.java, "listsdatabase.db")
            .build()
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    single <PlaylistsRepository>{
        PlaylistsRepositoryImpl(get(),get())
    }

    factory { TrackDbConvertor() }

    factory { PlaylistDbConverter() }
}