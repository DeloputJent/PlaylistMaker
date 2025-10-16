package com.practicum.playlistmaker.domain.models

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

data class Track (val trackName: String, // Название композиции
                  val artistName: String, // Имя исполнителя
                  val trackTimeMillis: String, // Продолжительность трека
                  val artworkUrl100: String, // Ссылка на изображение обложки
                  val trackId: String, // идентификатор iTunes
                  val collectionName: String, // Название альбома
                  val releaseDate: String, // Год релиза трека
                  val primaryGenreName: String, // Жанр трека
                  val country: String, // Страна исполнителя
                  val previewUrl: String, // Отрывок трека
                  val coverArtworkUrl: String // Ссылка на большую обложку
): Serializable