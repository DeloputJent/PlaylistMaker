package com.practicum.playlistmaker.presentation


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.search.domain.Track

class TrackListAdapter (
    private val tracks: MutableList<Track>,
    private val searchHistoryList: MutableList<Track> = mutableListOf(),
    private val clickListener: (Track) -> Unit={}
) : RecyclerView.Adapter<TrackListViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder =
        TrackListViewHolder.from(parent)

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener{clickListener(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}