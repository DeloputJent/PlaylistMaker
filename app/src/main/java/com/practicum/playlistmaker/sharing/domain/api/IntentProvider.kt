package com.practicum.playlistmaker.sharing.domain.api

interface IntentProvider {
    fun shareText()
    fun sendEmail()
    fun visitUrl()
}