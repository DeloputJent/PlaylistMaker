package com.practicum.playlistmaker.medialib.ui.presentation

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistFrameViewBinding
import com.practicum.playlistmaker.databinding.TrackFrameViewBinding
import com.practicum.playlistmaker.medialib.domain.Playlist
import java.io.File

class PlayListsViewHolder (
    private val binding: PlaylistFrameViewBinding,
):RecyclerView.ViewHolder(binding.root) {
    private val cover: ImageView = binding.playlistArtwork

    fun bind(playlist: Playlist) {
        binding.apply {
            playlistName.text = playlist.playlistName.trim()
            tracksInPlaylist.text = itemView.context.getString(
                R.string.tracks,
                playlist.tracksAmount.toString()
            )
        }

        val filePath = File(itemView.context
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES), "artwork_album")
        val uri = File(filePath, playlist.pathToArtwork)

        Glide.with(itemView)
            .load(uri)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .placeholder(R.drawable.placeholder)
            .into(cover)
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

    companion object {
        fun from(parent: ViewGroup): PlayListsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistFrameViewBinding.inflate(inflater, parent, false)
            return PlayListsViewHolder(binding)
        }
    }



}
