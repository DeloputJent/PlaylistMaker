package com.practicum.playlistmaker.settings.domain

interface IntentProvider {
    fun shareText()
    fun sendEmail()
    fun visitUrl()
}