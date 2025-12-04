package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.api.IntentProvider

class ExternalNavigator (private val context: Context) : IntentProvider {

    override fun shareText() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.this_app_made_with_course))
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.link_to_yandex_course))
        //shareIntent.flags=(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(Intent.createChooser(shareIntent,context.getString(R.string.share_with_help))
            .apply {flags= Intent.FLAG_ACTIVITY_NEW_TASK })
    }

    override fun sendEmail() {
        val sendIntent= Intent(Intent.ACTION_SENDTO)
        sendIntent.data= Uri.parse("mailto:")
        sendIntent.putExtra(Intent.EXTRA_EMAIL,arrayOf(context.getString(R.string.my_mail)))
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.mail_theme_to_devs))
        sendIntent.putExtra(Intent.EXTRA_TEXT,context.getString(R.string.thanks_to_devs))
        sendIntent.flags=(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(sendIntent)
    }

    override fun visitUrl() {
        val url = context.getString(R.string.link_to_offer).toUri()
        val urlIntent = Intent(Intent.ACTION_VIEW, url)
        urlIntent.flags=(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(urlIntent)
    }
}