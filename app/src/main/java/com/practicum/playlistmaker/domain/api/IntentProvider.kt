package com.practicum.playlistmaker.domain.api

interface IntentProvider {
    fun shareText(text: String)
    fun sendEmail(email: String, subject: String, body: String)
    fun visitUrl(address: String)
}