package com.practicum.playlistmaker.playlist.data

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.api.PlaylistIntentProvider

class PlayListExternalNavigator (private val context: Context) : PlaylistIntentProvider {



    override fun shareText() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.this_app_made_with_course))
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.link_to_yandex_course))
        context.startActivity(Intent.createChooser(shareIntent,context.getString(R.string.share_with_help))
            .apply {flags= Intent.FLAG_ACTIVITY_NEW_TASK })
    }

}