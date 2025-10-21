package com.practicum.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings.System.getString
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.IntentProvider
import androidx.core.net.toUri

class AndroidIntentProvider (private val context: Context) : IntentProvider {
        override fun shareText(text: String) {
            val shareIntent= Intent(Intent.ACTION_SEND)
            shareIntent.type="text/plain"
            val shareBody=getString(R.string.link_to_yandex_course)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.this_app_made_with_course))
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            context.startActivity(Intent.createChooser(shareIntent,getString(R.string.share_with_help) ))
        }

        override fun sendEmail(email: String, subject: String, body: String) {
            val sendIntent= Intent(Intent.ACTION_SENDTO)
            sendIntent.data= Uri.parse("mailto:")
            sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            sendIntent.putExtra(Intent.EXTRA_TEXT,body)
            context.startActivity(sendIntent)
        }

    override fun visitUrl(address: String) {
        val url = address.toUri()
        val intent = Intent(Intent.ACTION_VIEW, url)
        context.startActivity(intent)
    }
}