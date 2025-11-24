package com.practicum.playlistmaker.sharing.domain

import com.practicum.playlistmaker.sharing.domain.api.IntentProvider
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor

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