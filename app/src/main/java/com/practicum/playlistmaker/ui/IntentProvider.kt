package com.practicum.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.IntentProvider
import androidx.core.net.toUri

class IntentProvider (private val context: Context) : IntentProvider {

    override fun shareText() {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.this_app_made_with_course))
            shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.link_to_yandex_course))
            context.startActivity(Intent.createChooser(shareIntent,context.getString(R.string.share_with_help)))
    }

    override fun sendEmail() {
            val sendIntent= Intent(Intent.ACTION_SENDTO)
            sendIntent.data= Uri.parse("mailto:")
            sendIntent.putExtra(Intent.EXTRA_EMAIL,arrayOf(context.getString(R.string.my_mail)))
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.mail_theme_to_devs))
            sendIntent.putExtra(Intent.EXTRA_TEXT,context.getString(R.string.thanks_to_devs))
            context.startActivity(sendIntent)
    }

    override fun visitUrl() {
        val url = context.getString(R.string.link_to_offer).toUri()
        val intent = Intent(Intent.ACTION_VIEW, url)
        context.startActivity(intent)
    }
}