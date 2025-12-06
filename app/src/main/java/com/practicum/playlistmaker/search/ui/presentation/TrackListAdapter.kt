package com.practicum.playlistmaker.search.ui.presentation


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.search.domain.Track

class TrackListAdapter (
    private val clickListener: (Track) -> Unit={}
) : RecyclerView.Adapter<TrackListViewHolder> () {

    private val tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder =
        TrackListViewHolder.from(parent)

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener{clickListener(tracks[position])
        }
    }

    fun setTrackList(tracks: List<Track>) {
        this.tracks.clear()
        this.tracks.addAll(tracks)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}