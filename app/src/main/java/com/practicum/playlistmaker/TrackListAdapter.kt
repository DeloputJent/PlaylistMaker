package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackListAdapter (
    private val tracks: MutableList<Track>,
    private val searchHistoryList: MutableList<Track> = mutableListOf(),
    private val clickListener: (Track) -> Unit={}
) : RecyclerView.Adapter<TrackListViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_frame_view, parent, false)
        return TrackListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener{clickListener(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}