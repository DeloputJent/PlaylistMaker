package com.practicum.playlistmaker.presentation

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackFrameViewBinding
import com.practicum.playlistmaker.search.domain.Track

class TrackListViewHolder (private val binding: TrackFrameViewBinding): RecyclerView.ViewHolder(binding.root) {

    private val cover: ImageView = itemView.findViewById(R.id.AlbumCover)

    fun bind(track: Track) {
        binding.apply {
            SongName.text = track.trackName.trim()
            TheArtist.text = track.artistName.trim()
            SongLength.text = track.trackTimeMillis.trim()
        }

        Glide.with(itemView)
            .load(track.artworkUrl100)
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
        fun from(parent: ViewGroup): TrackListViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TrackFrameViewBinding.inflate(inflater, parent, false)
            return TrackListViewHolder(binding)
        }
    }

}