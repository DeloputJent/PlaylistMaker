package com.practicum.playlistmaker.medialib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.medialib.ui.presentation.PlayListsAdapter
import com.practicum.playlistmaker.medialib.ui.presentation.PlayListsScrollState
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class PlayListsFragment : Fragment() {

    private val viewModel by viewModel< PlayListsViewModel>()

    private lateinit var binding: FragmentPlaylistsBinding

    private lateinit var playListAdapter : PlayListsAdapter

    private lateinit var recyclerView : RecyclerView


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ButtonAddNewPlayList.setOnClickListener{
            findNavController().navigate(R.id.action_mediaLibFragment_to_newPlayListFragment)
        }

        recyclerView = binding.PlayListsScroll

        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)

        playListAdapter = PlayListsAdapter(
            clickListener = { playlist -> {}} )

        recyclerView.adapter= playListAdapter

        viewModel.getPlaylists()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

    }

    fun render(state: PlayListsScrollState) {
        when (state) {
            is PlayListsScrollState.Content -> showContent(state.playLists)
            is PlayListsScrollState.NoPlaylistsFound -> showNoPlaylistsMessage()
        }
    }

    fun showContent(playLists: List<Playlist>) {
        binding.noPlayListMessage.visibility = View.GONE
        playListAdapter.setPlayLists(playLists)
        recyclerView.visibility= View.VISIBLE
    }

    fun showNoPlaylistsMessage() {
        binding.apply {
            recyclerView.visibility = View.GONE
            noPlayListMessage.visibility = View.VISIBLE
        }
    }

    companion object{
        fun newInstace():PlayListsFragment{
            val fragment= PlayListsFragment()
            val bundle=Bundle()
            fragment.arguments=bundle
            return fragment
        }
    }
}