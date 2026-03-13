package com.practicum.playlistmaker.player.ui.presentation


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.search.domain.Track

class PlayListOnMPAdapter (
    private val clickListener: (Playlist) -> Unit={}
) : RecyclerView.Adapter<PlayListsOnMpViewHolder> () {

    private val playlists: MutableList<Playlist> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListsOnMpViewHolder =
        PlayListsOnMpViewHolder.Companion.from(parent)

    override fun onBindViewHolder(holder: PlayListsOnMpViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener{clickListener(playlists[position])
        }
    }

    fun setPlayLists(playlists: List<Playlist>) {
        this.playlists.clear()
        this.playlists.addAll(playlists)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}