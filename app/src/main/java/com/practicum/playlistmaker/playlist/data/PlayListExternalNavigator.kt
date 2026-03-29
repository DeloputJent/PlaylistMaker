package com.practicum.playlistmaker.playlist.data

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.api.PlaylistIntentProvider

class PlayListExternalNavigator (private val context: Context) : PlaylistIntentProvider {

    override fun shareText(message: List<String>) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TITLE, context.getString(R.string.Playlist_title_message))
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.Playlist_subject))
        var sendedMessage = ""
        message.forEach { messageText -> sendedMessage=sendedMessage+messageText+"\n" }
        message.forEach { message ->shareIntent.putExtra(Intent.EXTRA_TEXT, sendedMessage)
        }


        context.startActivity(Intent.createChooser(shareIntent,context.getString(R.string.share_with_help))
            .apply {flags= Intent.FLAG_ACTIVITY_NEW_TASK })
    }

}