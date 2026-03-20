package com.practicum.playlistmaker.medialib.ui.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.medialib.domain.Playlist

class PlayListsAdapter( private val clickListener: (Playlist) -> Unit={}
) : RecyclerView.Adapter<PlayListsViewHolder> () {

    private val playLists: MutableList<Playlist> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListsViewHolder =
        PlayListsViewHolder.from(parent)

    override fun onBindViewHolder(holder: PlayListsViewHolder, position: Int) {
        holder.bind(playLists[position])
        holder.itemView.setOnClickListener { clickListener(playLists[position]) }
    }

    fun setPlayLists(playLists: List<Playlist>) {
        this.playLists.clear()
        this.playLists.addAll(playLists)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return playLists.size
    }
}