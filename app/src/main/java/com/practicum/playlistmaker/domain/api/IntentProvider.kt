package com.practicum.playlistmaker.domain.api

interface IntentProvider {
    fun shareText()
    fun sendEmail()
    fun visitUrl()
}