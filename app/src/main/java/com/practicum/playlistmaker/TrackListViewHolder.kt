package com.practicum.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

    private val songName: TextView = itemView.findViewById(R.id.SongName)
    private val artist: TextView = itemView.findViewById(R.id.TheArtist)
    private val trackLength: TextView = itemView.findViewById(R.id.SongLength)
    private val cover: ImageView = itemView.findViewById(R.id.AlbumCover)

    fun bind(track: Track) {
        songName.text = track.trackName
        artist.text = track.artistName
        trackLength.text = track.trackTime
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


}