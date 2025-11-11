package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.data.ExternalNavigator

class SharingInteractorImpl(
    private val externalNavigator: IntentProvider,
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareText()
    }

    override fun openTerms() {
        externalNavigator.visitUrl()
    }

    override fun openSupport() {
        externalNavigator.sendEmail()
    }
}