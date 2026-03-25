package com.practicum.playlistmaker.playlist.ui.presentation


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.ui.presentation.TrackListViewHolder

class TracksInPlaylistAdapter (
    private val clickListener: (Track) -> Unit={},
    private val longClickListener: (Track) -> Unit={},
) : RecyclerView.Adapter<TrackListViewHolder> () {

    private val tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackListViewHolder = TrackListViewHolder.Companion.from(parent)

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener{
            clickListener(tracks[position])
        }
        holder.itemView.setOnLongClickListener{
            longClickListener(tracks[position])
            true
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