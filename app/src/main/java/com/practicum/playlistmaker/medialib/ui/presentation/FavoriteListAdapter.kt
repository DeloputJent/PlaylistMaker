package com.practicum.playlistmaker.medialib.ui.presentation


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.search.domain.Track

class FavoriteListAdapter (
    private val clickListener: (Track) -> Unit={}
) : RecyclerView.Adapter<FavoriteListViewHolder> () {

    private val tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder =
        FavoriteListViewHolder.from(parent)

    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
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