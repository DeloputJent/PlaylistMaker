package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HistoryTrackListAdapter (
    private val tracksInHistory: List<Track>
) : RecyclerView.Adapter<TrackListViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_frame_view, parent, false)
        return TrackListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        var acceptedTracks:List<Track> = tracksInHistory
        acceptedTracks=acceptedTracks.reversed()
        holder.bind(acceptedTracks[position])
    }

    override fun getItemCount(): Int {
        return tracksInHistory.size
    }
}